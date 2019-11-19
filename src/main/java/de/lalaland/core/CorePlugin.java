package de.lalaland.core;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lalaland.core.modules.economy.EconomyModule;
import de.lalaland.core.ui.Message;
import de.lalaland.core.config.Config;
import de.lalaland.core.config.ConfigFileHandler;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.tasks.TaskManager;
import de.lalaland.core.ui.gui.manager.GuiManager;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.utils.UtilModule;
import de.lalaland.core.utils.items.display.ItemDisplayCompiler;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
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
  private PaperCommandManager commandManager;
  @Getter
  private ProtocolManager protocolManager;
  @Getter
  @Setter
  private ItemDisplayCompiler displayCompiler;
  @Getter
  private GuiManager guiManager;

  private Object2ObjectLinkedOpenHashMap<Class<? extends IModule>, IModule> moduleMap;

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
    Message.init(this);
    coreLogger = LoggerFactory.getLogger(getClass().getName());
    gson = new GsonBuilder().setPrettyPrinting().create();
    coreConfig = new ConfigFileHandler(this).createIfNotExists();
    protocolManager = ProtocolLibrary.getProtocolManager();
    taskManager = new TaskManager(this);
    userManager = new UserManager(this);
    commandManager = new PaperCommandManager(this);
    moduleMap = new Object2ObjectLinkedOpenHashMap<>();
    guiManager = new GuiManager(this);
  }

  private void enableModules() {

    final ImmutableMap<Class<? extends IModule>, IModule> modules = ImmutableMap.<Class<? extends IModule>, IModule>builder()
        .put(UtilModule.class, new UtilModule())
        .put(CombatModule.class, new CombatModule())
        .put(EconomyModule.class, new EconomyModule(this))
        .build();

    modules.forEach((clazz, module) -> {
      try {
        module.enable(this);
        this.moduleMap.put(clazz, module);
        coreLogger.info("Successfully enabled module '" + module.getModuleName() + "'.");
      } catch (final Exception e) {
        coreLogger.error("Cannot enable module '" + module.getModuleName() + "'.");
        coreLogger.error(e.getMessage());
      }
    });

  }

  private void disableModuels() {

    for (final IModule module : moduleMap.values()) {
      try {
        module.disable(this);
        coreLogger.info("Successfully disable module '" + module.getModuleName() + "'.");
      } catch (final Exception e) {
        coreLogger.error("Cannot disable module '" + module.getModuleName() + "'.");
        coreLogger.error(e.getMessage());
      }
    }

  }

  public <T extends IModule> T getModule(Class<T> moduleClass) {
    return (T) this.moduleMap.get(moduleClass);
  }

}
