package de.lalaland.core.io.gson;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.IWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GsonFileWriter implements IWriter {

  private final CorePlugin plugin;
  private final File path;
  private final String fileName;

  /**
   * Instantiates a new Gson file writer.
   *
   * @param plugin   the plugin
   * @param path     the path
   * @param fileName the file name
   */
  public GsonFileWriter(final CorePlugin plugin, final File path, final String fileName) {
    this.plugin = plugin;
    this.path = path;
    this.fileName = fileName;
  }

  /**
   * Write a class into a json file.
   *
   * @param dataObject the object to serialize
   */
  @Override
  public <T> void write(final T dataObject) {

    final File file = new File(path, fileName + ".json");

    final boolean dirCreated = file.getParentFile().mkdirs();

    if (dirCreated) {
      plugin.getLogger().info("Directory for '" + fileName + ".json' created.");
    }

    final boolean fileCreated;
    try {
      fileCreated = file.createNewFile();
    } catch (final IOException e) {
      plugin.getLogger().severe("Unable to create path/file.");
      plugin.getLogger().severe(e.getMessage());
      return;
    }

    if (fileCreated) {
      plugin.getLogger().info("'" + fileName + ".json' created.");
    }

    final String jsonString = plugin.getGson().toJson(dataObject, dataObject.getClass());
    final FileWriter writer;
    try {
      writer = new FileWriter(file);
      writer.write(jsonString);
      writer.close();
    } catch (final IOException e) {
      plugin.getLogger().severe(e.getMessage());
    }

  }

}
