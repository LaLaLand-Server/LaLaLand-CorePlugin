package de.lalaland.core.modules.resourcepack.packing;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.ResourceCopy;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourcepackZipper {

  private static final int META_FORMAT = 4;
  // TODO pack description
  private static final String META_DESC = "- -";

  public ResourcepackZipper(final CorePlugin plugin) {
    this.plugin = plugin;
    packFolderSet = new ObjectLinkedOpenHashSet<>();

    packFolderSet.add(packFolder = new File(plugin.getDataFolder() + File.separator + "resourcepack"));
    packFolderSet.add(assetFolder = new File(packFolder + File.separator + "assets"));
    packFolderSet.add(minecraftFolder = new File(assetFolder + File.separator + "minecraft"));
    packFolderSet.add(blockStateFolder = new File(minecraftFolder + File.separator + "blockStates"));
    packFolderSet.add(fontFolder = new File(minecraftFolder + File.separator + "font"));
    packFolderSet.add(langFolder = new File(minecraftFolder + File.separator + "lang"));
    packFolderSet.add(modelsFolder = new File(minecraftFolder + File.separator + "models"));
    packFolderSet.add(itemModelFolder = new File(modelsFolder + File.separator + "items"));
    packFolderSet.add(blockModelFolder = new File(modelsFolder + File.separator + "blocks"));
    packFolderSet.add(particlesFolder = new File(minecraftFolder + File.separator + "particles"));
    packFolderSet.add(soundsFolder = new File(minecraftFolder + File.separator + "sounds"));
    packFolderSet.add(texturesFolder = new File(minecraftFolder + File.separator + "textures"));

    mcmetaFile = new File(packFolder, "pack.mcmeta");
    soundsFile = new File(assetFolder, "sounds.json");
    resourceZipFile = new File(plugin.getDataFolder(), "serverpack.zip");
  }

  private final ObjectLinkedOpenHashSet<File> packFolderSet;
  private final CorePlugin plugin;

  private final File resourceZipFile;


  private final File packFolder;
  private final File assetFolder;
  private final File minecraftFolder;
  private final File blockStateFolder;
  private final File fontFolder;
  private final File langFolder;
  private final File modelsFolder;
  private final File itemModelFolder;
  private final File blockModelFolder;
  private final File particlesFolder;
  private final File soundsFolder;
  private final File texturesFolder;

  private final File mcmetaFile;
  private final File soundsFile;

  private void createMetaFile() throws IOException {
    OutputStream out;
    final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(mcmetaFile));
    osw.write(new PackMeta(META_FORMAT, META_DESC).getAsJsonString());
    osw.close();
  }

  private void modelItemSetup() throws URISyntaxException, IOException {

    final AssetLibrary assetLibrary = new AssetLibrary(plugin);
    final Path temp = Files.createTempDirectory(plugin.getDataFolder().toPath(), "temp_rp");
    final File tempFolder = temp.toFile();

    try {
      final ResourceCopy copy = new ResourceCopy();
      final JarFile jf = new JarFile(CorePlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());

      copy.copyResourceDirectory(jf, "resourcepack", tempFolder);
    } catch (final IOException ex) {
      ex.printStackTrace();
    }

    final File textureFolder = new File(tempFolder + File.separator + "textures");

    for (final File subFolder : textureFolder.listFiles()) {
      for (final File icon : subFolder.listFiles()) {
        final ModelItem model = ModelItem.valueOf(icon.getName().replace(".png", ""));
        final boolean isBlock = model.getBaseMaterial().isBlock();
        final String nmsName = model.getBaseMaterial().getKey().getKey();
        final File resourceTextureFolder = new File(texturesFolder + File.separator + nmsName);
        resourceTextureFolder.mkdirs();
        final File imageFile = new File(resourceTextureFolder, "" + model.getModelID() + ".png");
        FileUtils.copyFile(icon, imageFile);
        final File modNMFolder = isBlock ? blockModelFolder : itemModelFolder;
        final File resourceModelFolder = new File(modNMFolder + File.separator + nmsName);
        resourceModelFolder.mkdirs();
        final File resourceModelFile = new File(resourceModelFolder, "" + model.getModelID() + ".json");

        final JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", model.getModelParent());
        final JsonObject textureJson = new JsonObject();
        textureJson.addProperty("layer0", nmsName + "/" + resourceModelFile.getName());
        modelJson.add("textures", textureJson);

        final JsonObject elementsJson = model.getElementsJson();
        if (elementsJson != null) {
          modelJson.add("elements", elementsJson);
        }

        final JsonObject displayJson = model.getDisplayJson();
        if (displayJson != null) {
          modelJson.add("display", displayJson);
        }

        final Gson gson = plugin.getGson();
        final String data = gson.toJson(modelJson);
        final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(resourceModelFile));
        osw.write(data);
        osw.close();
      }
    }

    // TODO print model json for bas item right (is printing null atm)
    final Map<Material, JsonObject> cachedJsons = Maps.newHashMap();

    for (final ModelItem model : ModelItem.values()) {
      final String nmsName = model.getBaseMaterial().getKey().getKey();
      final boolean isBlock = model.getBaseMaterial().isBlock();
      final JsonArray overrideArray;
      if (!cachedJsons.containsKey(model.getBaseMaterial())) {
        final JsonObject modelObject = new JsonObject();
        if (isBlock) {
          modelObject.addProperty("parent", assetLibrary.getAssetModelParent(nmsName));
        } else {
          modelObject.addProperty("parent", assetLibrary.getAssetModelParent(nmsName));
          final JsonObject textureObject = new JsonObject();
          textureObject.addProperty("layer0", assetLibrary.getAssetModelLayer0(nmsName));
          modelObject.add("textures", textureObject);
        }

        overrideArray = new JsonArray();
        modelObject.add("overrides", overrideArray);

      } else {
        overrideArray = cachedJsons.get(model.getBaseMaterial()).get("overrides").getAsJsonArray();
      }

      final JsonObject overrideObject = new JsonObject();
      final String customModelName =
          assetLibrary.getAssetModelLayer0(nmsName).split("/")[0] + "/" + nmsName + "/" + model.getModelID();
      overrideObject.addProperty("model", customModelName);
      final JsonObject predicateObject = new JsonObject();
      predicateObject.addProperty("custom_mode_data", model.getModelID());
      overrideObject.add("predicate", predicateObject);
    }

    final Gson gson = plugin.getGson();

    for (final ModelItem model : ModelItem.values()) {
      final File modelFolder = model.getBaseMaterial().isBlock() ? blockModelFolder : itemModelFolder;
      final File modelFile = new File(modelFolder, model.getBaseMaterial().getKey().getKey() + ".json");
      final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(modelFile));
      osw.write(gson.toJson(cachedJsons.get(model.getBaseMaterial())));
      osw.close();
    }

    FileUtils.deleteDirectory(tempFolder);
  }

  private void setupBaseFiles() throws IOException {
    FileUtils.deleteDirectory(assetFolder);
    for (final File folder : packFolderSet) {
      folder.mkdirs();
    }
    createMetaFile();
  }

  public void zipResourcepack() throws IOException, URISyntaxException {
    setupBaseFiles();
    createMetaFile();
    modelItemSetup();

    final FileOutputStream fos = new FileOutputStream(resourceZipFile);
    final ZipOutputStream zos = new ZipOutputStream(fos);
    zipFile(assetFolder, assetFolder.getName(), zos);
    zipFile(mcmetaFile, mcmetaFile.getName(), zos);

    zos.close();
    fos.close();
  }

  private void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {
    if (fileToZip.isHidden()) {
      return;
    }
    if (fileToZip.isDirectory()) {
      if (fileName.endsWith("/")) {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.closeEntry();
      } else {
        zipOut.putNextEntry(new ZipEntry(fileName + "/"));
        zipOut.closeEntry();
      }
      final File[] children = fileToZip.listFiles();
      for (final File childFile : children) {
        zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
      }
      return;
    }
    final FileInputStream fis = new FileInputStream(fileToZip);
    final ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);
    final byte[] bytes = new byte[1024];
    int length;
    while ((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }
    fis.close();
  }


}
