package de.lalaland.core.utils.io;

import de.lalaland.core.CorePlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GsonFileReader implements IReader {

  private final CorePlugin plugin;
  private final File directory;
  private final String fileName;

  /**
   * Instantiates a new Gson file reader.
   *
   * @param plugin    the plugin
   * @param directory the directory for the file
   * @param fileName  the name of the file
   */

  public GsonFileReader(final CorePlugin plugin, final File directory, final String fileName) {
    this.plugin = plugin;
    this.directory = directory;
    this.fileName = fileName;
  }

  /**
   * Read object from a file.
   *
   * @param classToDeserialize the class to deserialize
   * @param defaultValue       gets returned if object from file is null
   * @return the object from the file
   */

  @Override
  public Object read(final Class<?> classToDeserialize, final Object defaultValue) {

    final File file = new File(directory, fileName + ".json");

    if (!file.exists()) {
      sendFileNotFoundMessage();
      plugin.getCoreLogger().info("Saving default file.");
      final IWriter writer = new GsonFileWriter(plugin, directory, fileName);
      writer.write(defaultValue);
      return defaultValue;
    }

    final BufferedReader reader;

    try {
      reader = new BufferedReader(new FileReader(file));
    } catch (final FileNotFoundException e) {
      sendFileNotFoundMessage();
      plugin.getCoreLogger().error(e.getMessage());
      return defaultValue;
    }

    return plugin.getGson().fromJson(reader, classToDeserialize);
  }

  private void sendFileNotFoundMessage() {
    plugin.getCoreLogger().error(
        "Cannot find file '" + fileName + "' in directory '" + directory.getAbsolutePath()
            + "'.");
  }

}
