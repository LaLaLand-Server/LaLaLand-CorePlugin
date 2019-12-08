package de.lalaland.core.modules.mobs.implementations;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.combat.stats.CombatStatManager;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MobManager {

  public MobManager(final CombatStatManager combatStatManager, final CustomMobManager customMobManager) {
    this.combatStatManager = combatStatManager;
    this.customMobManager = customMobManager;
  }

  private final CustomMobManager customMobManager;
  private final CombatStatManager combatStatManager;
  // TODO Skill linker.

  public void createMob(final GameMobType type, final int level, final Location location) {
    final GameMob mob = type.getGameMobFunction().apply(level);
    final LivingEntity bukkitEntity = mob.spawn(location, customMobManager);
    final CombatStatHolder holder = combatStatManager.initEntity(bukkitEntity);
    mob.initCombatStats(holder);
  }

}
