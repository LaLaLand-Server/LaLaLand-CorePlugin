package de.lalaland.core.modules.mobs.implementations.dev;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.modules.mobs.implementations.GameMob;
import de.lalaland.core.utils.holograms.infobars.InfoLineSpacing;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TestBaseMob extends GameMob {

  public TestBaseMob(final int initLevel) {
    super(initLevel);
  }

  @Override
  public void initCombatStats(final CombatStatHolder holder) {
    holder.setStatBaseValue(CombatStat.HEALTH, 15 * level);
  }

  @Override
  public LivingEntity spawn(final Location location, final CustomMobManager customMobManager) {
    final PigZombie entity = location.getWorld().spawn(location, PigZombie.class);
    return entity;
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
