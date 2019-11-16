package de.lalaland.core.modules.CombatModule;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.CombatModule.stats.CombatCalculationListener;
import de.lalaland.core.modules.CombatModule.stats.CombatDamageListener;
import de.lalaland.core.modules.CombatModule.stats.CombatStatManager;
import de.lalaland.core.modules.CombatModule.items.BaseItemCommand;
import de.lalaland.core.modules.CombatModule.stats.CombatStatsCommand;
import de.lalaland.core.modules.IModule;
import lombok.Getter;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatModule implements IModule {

  @Getter
  private CombatStatManager combatStatManager;

  @Override
  public String getModuleName() {
    return "ComabatModule";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    plugin.getCommandManager().registerCommand(new BaseItemCommand());
    plugin.getCommandManager().registerCommand(new CombatStatsCommand(this));
    this.combatStatManager = new CombatStatManager(plugin);
    Bukkit.getPluginManager().registerEvents(new CombatCalculationListener(combatStatManager), plugin);
    Bukkit.getPluginManager().registerEvents(new CombatDamageListener(combatStatManager), plugin);
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
