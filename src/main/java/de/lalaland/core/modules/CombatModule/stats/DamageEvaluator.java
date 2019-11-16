package de.lalaland.core.modules.CombatModule.stats;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DamageEvaluator {

  protected static double evaluate(double damage, double defence) {
    return damage * (CombatStat.DEFENCE_HARD_CAP / CombatStat.DEFENCE_HARD_CAP + (3 * defence));
  }

  protected static double calculateDamage(CombatStatHolder holder, double damage,
      CombatDamageType type) {
    return evaluate(damage, holder.getStatValue(type.getDefendingStat()));
  }

}
