package de.lalaland.core.modules.mobs;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.modules.mobs.implementations.GameMobCommand;
import de.lalaland.core.modules.mobs.implementations.MobManager;
import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import lombok.AccessLevel;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MobModule implements IModule {

  @Getter(AccessLevel.PACKAGE)
  private MobModelManager mobModelManager;
  @Getter(AccessLevel.PACKAGE)
  private CustomMobManager customMobManager;
  @Getter
  private MobManager mobManager;

  @Override
  public String getModuleName() {
    return "MobModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    mobModelManager = new MobModelManager(plugin);
    customMobManager = new CustomMobManager(mobModelManager);
    mobManager = new MobManager(plugin.getModule(CombatModule.class).getCombatStatManager(), customMobManager, plugin);
    plugin.getCommandManager().registerCommand(new GameMobCommand(mobManager));
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }

}
