package de.lalaland.core.modules.resourcepack.packing;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Map;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AssetLibrary {

  private static final String ASSET_URL = "https://github.com/InventivetalentDev/minecraft-assets/archive/1.14.4.zip";

  protected AssetLibrary(final CorePlugin plugin) {
    this.plugin = plugin;
    itemModelDefaultAssets = Maps.newHashMap();
    assetLibFolder = new File(plugin.getDataFolder() + File.separator + "assetlib");
    downloadAssets();
    unzipAssets();
    iterateAssets();
  }

  private final File assetLibFolder;
  private final CorePlugin plugin;

  private void downloadAssets() {
    try {
      new FileOutputStream("assets.zip").getChannel().transferFrom(Channels.newChannel(new URL(ASSET_URL).openStream()), 0, Long.MAX_VALUE);
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

  private void unzipAssets() {

  }

  private void iterateAssets() {

  }


  private final Map<String, JsonObject> itemModelDefaultAssets;

  public String getAssetModelParent(final String nmsKey) {
    return itemModelDefaultAssets.get(nmsKey).get("parent").getAsString();
  }

  public String getAssetModelLayer0(final String nmsKey) {
    return itemModelDefaultAssets.get(nmsKey)
        .get("textures")
        .getAsJsonObject()
        .get("layer0").getAsString();
  }

}
