package de.lalaland.core.config;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.IReader;
import de.lalaland.core.io.IWriter;
import de.lalaland.core.io.gson.GsonFileReader;
import de.lalaland.core.io.gson.GsonFileWriter;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class ConfigFileHandler {

  private final CorePlugin corePlugin;

  public ConfigFileHandler(final CorePlugin corePlugin) {
    this.corePlugin = corePlugin;
  }

  public Config createIfNotExists() {

    final IReader reader = new GsonFileReader(corePlugin, corePlugin.getDataFolder(), "config");
    final Config defaultConfig = Config.getDefaultConfig();
    return (Config) reader.read(Config.class, defaultConfig);
  }

  public void saveConfig(){
    final IWriter writer = new GsonFileWriter(corePlugin, corePlugin.getDataFolder(),"config");
    writer.write(corePlugin.getCoreConfig());
  }

}
