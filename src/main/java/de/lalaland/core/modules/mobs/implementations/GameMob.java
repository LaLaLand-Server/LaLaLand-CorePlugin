package de.lalaland.core.modules.mobs.implementations;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.mobs.custommobs.CustomMobManager;
import de.lalaland.core.utils.holograms.infobars.InfoLineSpacing;
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
public abstract class GameMob {

  public GameMob(final int initLevel) {
    level = initLevel;
  }

  protected final int level;

  public abstract void initCombatStats(CombatStatHolder holder);

  public abstract LivingEntity spawn(Location location, CustomMobManager customMobManager);

  public abstract InfoLineSpacing getBottomLineSpacing();

  public abstract String getName();

}
