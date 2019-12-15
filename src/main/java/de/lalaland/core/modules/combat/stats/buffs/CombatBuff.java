package de.lalaland.core.modules.combat.stats.buffs;

import de.lalaland.core.modules.combat.stats.CombatContext;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class CombatBuff extends Buff {

  public CombatBuff(final BuffType category, final int buffTier, final int duration, final CombatStatHolder holder) {
    super(category, buffTier, duration, holder);
  }

  public abstract void apply(CombatContext context);

}
