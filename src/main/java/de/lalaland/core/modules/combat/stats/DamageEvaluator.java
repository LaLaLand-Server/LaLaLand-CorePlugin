package de.lalaland.core.modules.combat.stats;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.Nullable;

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

  private static final PotionEvaluator POTION_EVALUATOR = new PotionEvaluator();

  protected static double evaluate(final double damage, final double defence) {
    return damage * (CombatStat.DEFENCE_HARD_CAP / (CombatStat.DEFENCE_HARD_CAP + (3 * defence)));
  }

  protected static double calculateDamage(final CombatStatHolder defender, @Nullable final CombatStatHolder attacker, double damage,
      final DamageCause cause) {
    if (attacker != null) {
      damage = POTION_EVALUATOR.evaluateAttacking(attacker.getBukkitEntity().getActivePotionEffects(), damage, cause);
    }
    damage = POTION_EVALUATOR.evaluateDefending(defender.getBukkitEntity().getActivePotionEffects(), damage, cause);
    return evaluate(damage, defender.getStatValue(CombatDamageType.ofBukkit(cause).getDefendingStat()));
  }

  protected static double calculateDamage(final CombatStatHolder defender, final double damage,
      final DamageCause cause) {
    return calculateDamage(defender, null, damage, cause);
  }

}
