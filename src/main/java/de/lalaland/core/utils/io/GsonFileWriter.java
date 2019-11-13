package de.lalaland.core.utils.io;

import de.lalaland.core.CorePlugin;
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
   * @param dataClass the class to serialize
   */
  @Override
  public void write(final Object dataClass) {

    final File file = new File(this.path, this.fileName + ".json");

    final boolean dirCreated = file.getParentFile().mkdirs();

    if (dirCreated) {
      this.plugin.getLogger().info("Directory for '" + this.fileName + ".json' created.");
    }

    final boolean fileCreated;
    try {
      fileCreated = file.createNewFile();
    } catch (final IOException e) {
      this.plugin.getLogger().severe("Unable to create path/file.");
      this.plugin.getLogger().severe(e.getMessage());
      return;
    }

    if (fileCreated) {
      this.plugin.getLogger().info("'" + this.fileName + ".json' created.");
    }

    final String jsonString = this.plugin.getGson().toJson(dataClass, dataClass.getClass());
    final FileWriter writer;
    try {
      writer = new FileWriter(file);
      writer.write(jsonString);
      writer.close();
    } catch (final IOException e) {
      this.plugin.getLogger().severe(e.getMessage());
    }

  }

}
