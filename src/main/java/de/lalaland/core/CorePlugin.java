package de.lalaland.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lalaland.core.config.Config;
import de.lalaland.core.config.ConfigFileHandler;
import de.lalaland.core.user.UserManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class CorePlugin extends JavaPlugin {

  //test edit
  
  @Getter
  private Logger coreLogger;
  @Getter
  private Gson gson;
  @Getter
  private Config coreConfig;
  @Getter
  private UserManager userManager;

  @Override
  public void onLoad() {

  }

  @Override
  public void onEnable() {
    init();
  }

  @Override
  public void onDisable() {

  }

  private void init() {
    coreLogger = LoggerFactory.getLogger(getClass().getName());
    gson = new GsonBuilder().setPrettyPrinting().create();
    coreConfig = new ConfigFileHandler().createIfNotExists();
    userManager = new UserManager(this);
  }

}
