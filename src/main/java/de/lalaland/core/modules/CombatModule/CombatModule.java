package de.lalaland.core.modules.CombatModule;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.CombatModule.stats.CombatStatManager;
import de.lalaland.core.modules.CombatModule.items.BaseItemCommand;
import de.lalaland.core.modules.IModule;
import lombok.Getter;

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
    this.combatStatManager = new CombatStatManager(plugin);
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
