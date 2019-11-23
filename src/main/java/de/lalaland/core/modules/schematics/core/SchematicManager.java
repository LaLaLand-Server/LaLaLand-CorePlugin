package de.lalaland.core.modules.schematics.core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.schematics.workload.PasteThread;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicManager implements Iterable<SimpleSchematic> {

  private static final int WRITER_BUFFER_SIZE = 20480;

  public SchematicManager(final CorePlugin plugin) {
    schematicCashe = new Object2ObjectOpenHashMap<>();
    pasteThread = new PasteThread();
    plugin.getTaskManager().runRepeatedBukkit(pasteThread, 1L, 1L);
    schematicFolder = new File(plugin.getDataFolder() + File.separator + "schematics");
    if (!schematicFolder.exists()) {
      schematicFolder.mkdirs();
    }
    gson = plugin.getGson();
  }

  @Getter
  private final PasteThread pasteThread;
  private final Object2ObjectOpenHashMap<String, SimpleSchematic> schematicCashe;
  private final File schematicFolder;
  private final Gson gson;

  @Nullable
  public SimpleSchematic getSchematic(final String schematicID) {
    return schematicCashe.get(schematicID);
  }

  public SimpleSchematic createSimple(final Block corner1, final Block corner2,
      final String schematicID) {
    final SimpleSchematic schematic = new SimpleSchematic(corner1, corner2, schematicID,
        pasteThread);
    schematicCashe.put(schematicID, schematic);
    return schematic;
  }

  public void saveSchematics() throws IOException {
    for (final SimpleSchematic schematic : schematicCashe.values()) {
      final BufferedWriter writer;
      final File schematicFile = new File(schematicFolder, schematic.getSchmaticID() + ".json");
      writer = new BufferedWriter(new FileWriter(schematicFile), WRITER_BUFFER_SIZE);
      writer.write(gson.toJson(schematic.getAsJson()));
      writer.close();
    }
  }

  public void loadSchematics() throws IOException {
    for (final File schematicFile : schematicFolder.listFiles()) {
      final BufferedReader reader = new BufferedReader(new FileReader(schematicFile));
      int read;
      final StringBuilder builder = new StringBuilder();
      while ((read = reader.read()) != -1) {
        builder.append((char) read);
      }
      final JsonElement element = gson.fromJson(builder.toString(), JsonElement.class);
      final SimpleSchematic schematic = new SimpleSchematic(element, pasteThread);
      schematicCashe.put(schematic.getSchmaticID(), schematic);
    }
  }

  @NotNull
  @Override
  public Iterator<SimpleSchematic> iterator() {
    return schematicCashe.values().iterator();
  }

  @Override
  public void forEach(final Consumer<? super SimpleSchematic> action) {
    schematicCashe.values().forEach(action);
  }
}
