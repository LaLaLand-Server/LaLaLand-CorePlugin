package de.lalaland.core.modules.combat.stats;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import java.util.Collection;
import java.util.EnumSet;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PotionEvaluator {

  protected PotionEvaluator() {
    for (int lvl = 0; lvl <= 100; lvl++) {
      RESIST_MAP.put(lvl, 1D / Math.pow(1.1, lvl));
      ATTK_MAP.put(lvl, 1D * Math.pow(1.1, lvl));
      WEAK_MAP.put(lvl, 1D / (1.1 * Math.pow(1.1, lvl)));
    }
  }

  private final Int2DoubleMap RESIST_MAP = new Int2DoubleOpenHashMap();
  private final Int2DoubleMap ATTK_MAP = new Int2DoubleOpenHashMap();
  private final Int2DoubleMap WEAK_MAP = new Int2DoubleOpenHashMap();

  protected double evaluateDefending(final Collection<PotionEffect> effects, double damage, final DamageCause type) {
    for (final PotionEffect effect : effects) {
      final PotionEffectType effType = effect.getType();
      if (effType == PotionEffectType.DAMAGE_RESISTANCE) {
        damage *= RESIST_MAP.get(effect.getAmplifier() + 1);
      } else if (effType == PotionEffectType.FIRE_RESISTANCE && isFire(type)) {
        damage *= RESIST_MAP.get(effect.getAmplifier() + 1);
      }
    }
    return damage;
  }

  protected double evaluateAttacking(final Collection<PotionEffect> effects, double damage, final DamageCause type) {
    for (final PotionEffect effect : effects) {
      final PotionEffectType effType = effect.getType();
      if (effType == PotionEffectType.INCREASE_DAMAGE) {
        damage *= ATTK_MAP.get(effect.getAmplifier() + 1);
      } else if (effType == PotionEffectType.WEAKNESS) {
        damage *= WEAK_MAP.get(effect.getAmplifier() + 1);
      }
    }
    return damage;
  }

  private final EnumSet FIRE_TYPES = EnumSet.of(DamageCause.FIRE,
      DamageCause.FIRE_TICK,
      DamageCause.HOT_FLOOR,
      DamageCause.LAVA);

  private boolean isFire(final DamageCause type) {
    return FIRE_TYPES.contains(type);
  }

}
