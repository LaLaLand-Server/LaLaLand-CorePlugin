package de.lalaland.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import de.lalaland.core.config.Config;
import de.lalaland.core.config.ConfigFileHandler;
import de.lalaland.core.modules.CombatModule.CombatModule;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.tasks.TaskManager;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.utils.UtilModule;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
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

  @Getter
  private Logger coreLogger;
  @Getter
  private Gson gson;
  @Getter
  private Config coreConfig;
  @Getter
  private UserManager userManager;
  @Getter
  private TaskManager taskManager;

  @Getter
  private List<IModule> modules;

  @Override
  public void onLoad() {

  }

  @Override
  public void onEnable() {
    init();
    enableModules();
  }

  @Override
  public void onDisable() {
    disableModuels();
  }

  private void init() {
    coreLogger = LoggerFactory.getLogger(getClass().getName());
    gson = new GsonBuilder().setPrettyPrinting().create();
    coreConfig = new ConfigFileHandler(this).createIfNotExists();
    userManager = new UserManager(this);
    taskManager = new TaskManager(this);
    modules = new ArrayList<>();
  }

  private void enableModules() {

    final IModule[] modules = new IModule[]{
        new UtilModule(),
        new CombatModule()};

    for (int i = 0; i < modules.length; i++) {
      final IModule module = modules[i];
      try {

        module.enable(this);
        this.modules.add(module);
        coreLogger.info("Successfully enabled module '" + module.getModuleName() + "'.");
      } catch (final Exception e) {
        coreLogger.error("Cannot enable module '" + module.getModuleName() + "'.");
        coreLogger.error(e.getMessage());
      }

    }

  }

  private void disableModuels() {

    for (final IModule module : modules) {
      try {
        module.disable(this);
        coreLogger.info("Successfully disable module '" + module.getModuleName() + "'.");
      } catch (final Exception e) {
        coreLogger.error("Cannot disable module '" + module.getModuleName() + "'.");
        coreLogger.error(e.getMessage());
      }
    }

  }

  public void registerListener(final Listener listener) {
    final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    pluginManager.registerEvents(listener, this);
  }

  public void registerCommand(final String label, @NotNull final CommandExecutor executor) {
    getCommand(label).setExecutor(executor);
  }

}
