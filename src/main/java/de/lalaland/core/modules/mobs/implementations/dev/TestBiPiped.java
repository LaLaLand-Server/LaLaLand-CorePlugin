package de.lalaland.core.modules.mobs.implementations.dev;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.mobs.custommobs.ComplexModelType;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.modules.mobs.implementations.GameMob;
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
public class TestBiPiped extends GameMob {

  public TestBiPiped(final int initLevel) {
    super(initLevel);
  }

  @Override
  public void initCombatStats(final CombatStatHolder holder) {
    holder.setStatBaseValue(CombatStat.HEALTH, level * 15);
  }

  @Override
  public LivingEntity spawn(final Location location, final CustomMobManager customMobManager) {
    return customMobManager.spawnModeled(ComplexModelType.TEST_BI, location).getBukkit();
  }

}
