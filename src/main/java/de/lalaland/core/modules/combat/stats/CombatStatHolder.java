package de.lalaland.core.modules.combat.stats;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.lalaland.core.modules.combat.stats.buffs.Buff;
import de.lalaland.core.modules.combat.stats.buffs.BuffEnvironment;
import de.lalaland.core.modules.combat.stats.buffs.BuffType;
import de.lalaland.core.modules.combat.stats.buffs.CombatBuff;
import de.lalaland.core.modules.combat.stats.buffs.StatBuff;
import de.lalaland.core.utils.tuples.Pair;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatStatHolder {

  protected CombatStatHolder(final LivingEntity bukkitEntity, final CombatStatCalculator combatStatCalculator) {
    this.bukkitEntity = bukkitEntity;
    human = bukkitEntity instanceof Player;
    if (human) {
      ((Player) bukkitEntity).setHealthScale(20D);
    }
    combatStatMappings = Maps.newEnumMap(CombatStat.getEmptyMap());
    baseValues = Maps.newEnumMap(CombatStat.getBaseMap(human));
    recalculatingSheduled = false;
    this.combatStatCalculator = combatStatCalculator;
    combatBuffMap = new EnumMap<>(BuffType.class);
    statBuffMap = new EnumMap<>(BuffType.class);
  }

  @Setter(AccessLevel.PROTECTED)
  @Getter(AccessLevel.PROTECTED)
  private boolean recalculatingSheduled;
  @Getter(AccessLevel.PROTECTED)
  private final LivingEntity bukkitEntity;
  @Getter
  private final boolean human;
  private final EnumMap<CombatStat, Double> combatStatMappings;
  private final EnumMap<CombatStat, Double> baseValues;
  private final CombatStatCalculator combatStatCalculator;
  private final EnumMap<BuffType, CombatBuff> combatBuffMap;
  private final EnumMap<BuffType, StatBuff> statBuffMap;

  public void applyCombatBuffs(final CombatContext combatContext, final boolean asAttacker) {
    for (final CombatBuff cBuff : combatBuffMap.values()) {
      if (asAttacker) {
        if (!cBuff.getBuffType().isDefensive()) {
          cBuff.apply(combatContext);
        }
      } else {
        if (cBuff.getBuffType().isDefensive()) {
          cBuff.apply(combatContext);
        }
      }
    }
  }

  protected void applyStatBuffs(final Map<CombatStat, Double> statMap) {
    for (final StatBuff statBuff : statBuffMap.values()) {
      statBuff.apply(statMap);
    }
  }

  public void addBuff(final Buff buff) {
    if (buff.getBuffType().getBuffEnvironment() == BuffEnvironment.COMBAT) {
      final BuffType buffType = buff.getBuffType();
      final Buff currentBuff = combatBuffMap.get(buffType);
      if (buff.getBuffTier() >= currentBuff.getBuffTier()) {
        combatBuffMap.put(buffType, (CombatBuff) buff);
      }
    } else {
      final BuffType buffType = buff.getBuffType();
      final Buff currentBuff = statBuffMap.get(buffType);
      if (buff.getBuffTier() >= currentBuff.getBuffTier()) {
        statBuffMap.put(buffType, (StatBuff) buff);
      }
    }
  }

  public void removeBuff(final Buff buff) {
    if (buff.getBuffType().getBuffEnvironment() == BuffEnvironment.COMBAT) {
      final Buff oldBuff = combatBuffMap.get(buff.getBuffType());
      if (oldBuff.equals(buff)) {
        combatBuffMap.remove(buff.getBuffType());
      }
    } else {
      final Buff oldBuff = statBuffMap.get(buff.getBuffType());
      if (oldBuff.equals(buff)) {
        statBuffMap.remove(buff.getBuffType());
      }
    }
  }

  /**
   * Gets the complete value of a stat.
   *
   * @param stat the stat type
   * @return
   */
  public double getStatValue(final CombatStat stat) {
    return combatStatMappings.get(stat) + baseValues.get(stat);
  }

  public double getStatExtraValue(final CombatStat stat) {
    return combatStatMappings.get(stat);
  }

  /**
   * Gets only the base value of a stat.
   *
   * @param stat
   * @return
   */
  public double getStatBaseValue(final CombatStat stat) {
    return baseValues.getOrDefault(stat, 0D);
  }

  /**
   * Sets the base value of a stat.
   *
   * @param stat
   * @param value
   */
  public void setStatBaseValue(final CombatStat stat, final double value) {
    baseValues.put(stat, value + stat.getBaseValue(human));
  }

  public void recalculate() {
    combatStatCalculator.recalculateValues(this);
  }

  /**
   * This will return a Pair of two Maps. The first map includes a copy of the base values of this holder. The second map includes a copy of
   * the complete calculated combat map.
   * <p>
   * Those Maps do not have any reflective impact on the base maps.
   *
   * @return a pair of two ImmutableMaps
   */
  public Pair<ImmutableMap<CombatStat, Double>, ImmutableMap<CombatStat, Double>> getValueMappings() {
    return Pair.of(ImmutableMap.copyOf(baseValues), ImmutableMap.copyOf(combatStatMappings));
  }

  /**
   * Rebases a new value of a stat.
   *
   * @param stat  the stat
   * @param value the value
   * @return the old value.
   */
  protected double setExtraValue(final CombatStat stat, final double value) {
    return combatStatMappings.put(stat, value);
  }

  /**
   * Resets the stat map with base value imports.
   */
  protected void resetStatMap() {
    for (final CombatStat stat : CombatStat.values()) {
      combatStatMappings.put(stat, getStatBaseValue(stat));
    }
  }

  protected void tickBuffs() {
    final Set<Buff> removers = Sets.newHashSet();
    for (final Buff buff : combatBuffMap.values()) {
      if (buff.tickDuration()) {
        removers.add(buff);
      }
    }
    for (final Buff buff : statBuffMap.values()) {
      if (buff.tickDuration()) {
        removers.add(buff);
      }
    }
    for (final Buff remover : removers) {
      remover.remove();
    }
  }

}