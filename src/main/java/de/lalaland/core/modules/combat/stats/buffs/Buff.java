package de.lalaland.core.modules.combat.stats.buffs;

import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@EqualsAndHashCode
public class Buff {

  public Buff(final BuffType buffType, final int buffTier, final int duration, final CombatStatHolder holder) {
    this.buffTier = buffTier;
    this.duration = duration;
    durationLeft = duration;
    this.holder = holder;
    this.buffType = buffType;
  }

  @Getter
  private final BuffType buffType;
  private final CombatStatHolder holder;
  @Getter
  private final int buffTier;
  private final int duration;
  private int durationLeft;

  public boolean tickDuration() {
    return --durationLeft == 0;
  }

  public void remove() {
    holder.removeBuff(this);
  }

}
