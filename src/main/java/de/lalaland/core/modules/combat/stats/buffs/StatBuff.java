package de.lalaland.core.modules.combat.stats.buffs;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import java.util.Map;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class StatBuff extends Buff {

  public StatBuff(final BuffType buffType, final int buffTier, final int duration,
      final CombatStatHolder holder) {
    super(buffType, buffTier, duration, holder);
  }

  public abstract void apply(Map<CombatStat, Double> statMap);

}
