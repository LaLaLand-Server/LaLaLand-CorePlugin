package de.lalaland.core.modules.resourcepack.packing;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;

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

  private static final String ASSET_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.14.4/assets/minecraft/models/";

  protected AssetLibrary(final CorePlugin plugin) {
    this.plugin = plugin;
    itemModelDefaultAssets = Maps.newHashMap();
    assetLibFolder = new File(plugin.getDataFolder() + File.separator + "assetlib");
    assetLibFolder.mkdirs();
    iterateAssets();
  }

  private final File assetLibFolder;
  private final CorePlugin plugin;
  @Getter(AccessLevel.PROTECTED)
  private JsonObject defaultFont;

  private final Map<String, JsonObject> itemModelDefaultAssets;

  private void iterateAssets() {

    for (final ModelItem model : ModelItem.values()) {
      final String modelFolder = model.getBaseMaterial().isBlock() ? "block" : "item";
      final String nmsName = model.getBaseMaterial().getKey().getKey();
      if (!itemModelDefaultAssets.containsKey(nmsName)) {
        final String jsonUrl = ASSET_URL + modelFolder + "/" + nmsName + ".json";
        final JsonObject json = plugin.getGson().fromJson(jsonGetRequest(jsonUrl), JsonObject.class);
        itemModelDefaultAssets.put(nmsName, json);
      }
    }

  }

  private String jsonGetRequest(final String urlQueryString) {
    String json = null;
    try {
      final URL url = new URL(urlQueryString);
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects(false);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("charset", "utf-8");
      connection.connect();
      final InputStream inStream = connection.getInputStream();
      final InputStreamReader isr = new InputStreamReader(inStream, "UTF-8");
      int read;
      final StringBuilder builder = new StringBuilder();
      while ((read = isr.read()) != -1) {
        builder.append((char) read);
      }
      json = builder.toString();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
    return json;
  }

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
