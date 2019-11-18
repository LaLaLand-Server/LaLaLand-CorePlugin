package de.lalaland.core.modules.combat;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.combat.items.BaseItemCommand;
import de.lalaland.core.modules.combat.items.ItemInfoCompiler;
import de.lalaland.core.modules.combat.stats.CombatCalculationListener;
import de.lalaland.core.modules.combat.stats.CombatDamageListener;
import de.lalaland.core.modules.combat.stats.CombatStatManager;
import de.lalaland.core.modules.combat.stats.CombatStatsCommand;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.utils.UtilModule;
import de.lalaland.core.utils.holograms.impl.HologramManager;
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
    this.combatStatManager = new CombatStatManager(plugin);
    plugin.getCommandManager().registerCommand(new BaseItemCommand());
    plugin.getCommandManager().registerCommand(new CombatStatsCommand(this));
    Bukkit.getPluginManager()
        .registerEvents(new CombatCalculationListener(combatStatManager), plugin);
    HologramManager holo = plugin.getModule(UtilModule.class).getHologramManager();
    Bukkit.getPluginManager()
        .registerEvents(new CombatDamageListener(combatStatManager, holo), plugin);
    plugin.getDisplayCompiler().registerConverter(new ItemInfoCompiler());
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
