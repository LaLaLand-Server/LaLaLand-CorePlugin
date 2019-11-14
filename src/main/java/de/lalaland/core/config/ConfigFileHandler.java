package de.lalaland.core.config;

import de.lalaland.core.CorePlugin;
import java.io.File;

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

    final File dir = new File(corePlugin.getDataFolder() + File.separator +"config.json");

    return null;
  }

}
