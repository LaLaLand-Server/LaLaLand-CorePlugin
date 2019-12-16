package de.lalaland.core.modules.skills;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.modules.skills.skillimpl.SkillTriggerListener;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SkillModule implements IModule {

  @Override
  public String getModuleName() {
    return "SkillModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {
    Bukkit.getPluginManager().registerEvents(new SkillTriggerListener(plugin.getModule(CombatModule.class).getCombatStatManager()), plugin);
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
