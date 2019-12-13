package de.lalaland.core.modules.mobs.implementations.dev;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.modules.mobs.implementations.GameMob;
import de.lalaland.core.utils.holograms.infobars.InfoLineSpacing;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
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
public class TestDisguised extends GameMob {

  public TestDisguised(final int initLevel) {
    super(initLevel);
  }

  @Override
  public void initCombatStats(final CombatStatHolder holder) {
    holder.setStatBaseValue(CombatStat.HEALTH, level * 15);
  }

  @Override
  public LivingEntity spawn(final Location location, final CustomMobManager customMobManager) {
    final LivingEntity mob = customMobManager.spawnPlayerDisguised(EntityType.PIG_ZOMBIE, "h0n1kkuchen", location);
    return mob;
  }

  @Override
  public InfoLineSpacing getBottomLineSpacing() {
    return InfoLineSpacing.LARGE;
  }

  @Override
  public String getName() {
    return "Base Mob";
  }

}