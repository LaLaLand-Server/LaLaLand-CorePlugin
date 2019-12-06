package de.lalaland.core.modules.resourcepack.packing;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.ResourceCopy;
import de.lalaland.core.modules.resourcepack.skins.FontMeta;
import de.lalaland.core.modules.resourcepack.skins.ModelBlock;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
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
    packFolderSet.add(blockStateFolder = new File(minecraftFolder + File.separator + "blockstates"));
    packFolderSet.add(fontFolder = new File(minecraftFolder + File.separator + "font"));
    packFolderSet.add(langFolder = new File(minecraftFolder + File.separator + "lang"));
    packFolderSet.add(modelsFolder = new File(minecraftFolder + File.separator + "models"));
    packFolderSet.add(itemModelFolder = new File(modelsFolder + File.separator + "item"));
    packFolderSet.add(blockModelFolder = new File(modelsFolder + File.separator + "block"));
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
    final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(mcmetaFile));
    osw.write(new PackMeta(META_FORMAT, META_DESC).getAsJsonString());
    osw.close();
  }

  private void modelItemSetup() throws URISyntaxException, IOException {
    final AssetLibrary assetLibrary = new AssetLibrary(plugin);
    final Path temp = Files.createTempDirectory(plugin.getDataFolder().toPath(), "temp_rp");
    final File tempFolder = temp.toFile();
    final JsonObject fontJson = new JsonObject();
    final JsonArray providerArray = new JsonArray();
    char fontIndex = (char) 0x3360;

    try {
      final ResourceCopy copy = new ResourceCopy();
      final JarFile jf = new JarFile(CorePlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath());

      copy.copyResourceDirectory(jf, "resourcepack", tempFolder);
    } catch (final IOException ex) {
      ex.printStackTrace();
    }

    //Blocks
    final File modelBlockFolder = new File(tempFolder, "blockstates");
    final Map<String, JsonObject> blockstateJsonMap = Maps.newHashMap();
    for (final ModelBlock modelBlock : ModelBlock.values()) {
      final String vanillaName = modelBlock.getBaseMaterial().getKey().getKey();
      final File modelBlockImage = new File(modelBlockFolder, modelBlock.toString() + ".png");
      if (!modelBlockImage.exists()) {
        plugin.getLogger().severe("Could not find image of " + modelBlock.toString());
        continue;
      }
      final JsonObject stateJson;
      if (blockstateJsonMap.containsKey(vanillaName)) {
        stateJson = blockstateJsonMap.get(vanillaName);
      } else {
        stateJson = new JsonObject();
        blockstateJsonMap.put(vanillaName, stateJson);
      }
      final JsonObject variantsJson;
      if (stateJson.has("variants")) {
        variantsJson = stateJson.get("variants").getAsJsonObject();
      } else {
        variantsJson = new JsonObject();
      }
      final JsonObject modelJson = new JsonObject();
      modelJson.addProperty("model", "block/" + modelBlock.toString());
      variantsJson.add(modelBlock.getBlockStateApplicant(), modelJson);
      stateJson.add("variants", variantsJson);

      final JsonObject customModelJson = new JsonObject();
      customModelJson.addProperty("parent", "block/cube_all");
      final JsonObject textureJson = new JsonObject();
      textureJson.addProperty("all", modelBlock.toString().toLowerCase());
      customModelJson.add("textures", textureJson);
      FileUtils.copyFile(modelBlockImage, new File(texturesFolder, modelBlock.toString().toLowerCase() + ".png"));
      final OutputStreamWriter osw = new OutputStreamWriter(
          new FileOutputStream(new File(blockModelFolder, modelBlock.toString() + ".json")), "UTF-8");
      osw.write(plugin.getGson().toJson(customModelJson));
      osw.close();
    }

    for (final Entry<String, JsonObject> entry : blockstateJsonMap.entrySet()) {
      final File stateFile = new File(blockStateFolder, entry.getKey() + ".json");
      final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(stateFile), "UTF-8");
      osw.write(plugin.getGson().toJson(entry.getValue()));
      osw.close();
    }

    // Items
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

        final String iconPath = "minecraft:" + nmsName + "/" + model.getModelID() + ".png";
        model.getBoxedFontChar().value = fontIndex;
        final FontMeta fontMeta = model.getFontMeta();
        final JsonObject fontProvider = new JsonObject();
        fontProvider.addProperty("file", iconPath);
        final JsonArray charArray = new JsonArray();
        charArray.add(fontIndex);
        fontProvider.add("chars", charArray);
        fontProvider.addProperty("height", fontMeta.getHeight());
        fontProvider.addProperty("ascent", fontMeta.getAscent());
        fontProvider.addProperty("type", fontMeta.getType());
        providerArray.add(fontProvider);
        fontIndex++;

        final JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", model.getModelData().getModelParent());
        final JsonObject textureJson = new JsonObject();
        textureJson.addProperty("layer0", nmsName + "/" + model.getModelID());
        modelJson.add("textures", textureJson);

        final JsonObject elementsJson = model.getModelData().getElementsJson();
        if (elementsJson != null) {
          modelJson.add("elements", elementsJson);
        }

        final JsonObject displayJson = model.getModelData().getDisplayJson();
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

    final JsonObject ttfProvider = new JsonObject();
    ttfProvider.addProperty("type", "ttf");
    ttfProvider.addProperty("size", 9.5);
    ttfProvider.addProperty("oversample", 6.0);
    final JsonArray shiftArray = new JsonArray();
    shiftArray.add(0);
    shiftArray.add(0.75);
    ttfProvider.add("shift", shiftArray);
    ttfProvider.addProperty("file", "minecraft:uniformcenter.ttf");
    providerArray.add(ttfProvider);

    final File ttfFile = new File(tempFolder, "uniformcenter.ttf");
    FileUtils.copyFile(ttfFile, new File(fontFolder, "uniformcenter.ttf"));

    fontJson.add("providers", providerArray);
    final File fontFile = new File(fontFolder, "default.json");
    final OutputStreamWriter oswFont = new OutputStreamWriter(new FileOutputStream(fontFile), "UTF-8");
    oswFont.write(plugin.getGson().toJson(fontJson));
    oswFont.close();

    final Map<Material, JsonObject> cachedJsons = Maps.newHashMap();

    for (final ModelItem model : ModelItem.values()) {
      final String nmsName = model.getBaseMaterial().getKey().getKey();
      final boolean isBlock = model.getBaseMaterial().isBlock();
      final JsonArray overrideArray;
      final JsonObject modelObject;

      if (!cachedJsons.containsKey(model.getBaseMaterial())) {
        modelObject = new JsonObject();
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
        modelObject = cachedJsons.get(model.getBaseMaterial());
        overrideArray = modelObject.get("overrides").getAsJsonArray();
      }

      final JsonObject overrideObject = new JsonObject();
      final String customModelName =
          assetLibrary.getAssetModelLayer0(nmsName).split("/")[0] + "/" + nmsName + "/" + model.getModelID();
      overrideObject.addProperty("model", customModelName);
      final JsonObject predicateObject = new JsonObject();
      predicateObject.addProperty("custom_model_data", model.getModelID());
      overrideObject.add("predicate", predicateObject);
      overrideArray.add(overrideObject);

      modelObject.add("overrides", overrideArray);
      cachedJsons.put(model.getBaseMaterial(), modelObject);
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
