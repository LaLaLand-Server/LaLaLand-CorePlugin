package de.lalaland.core.modules.combat.stats;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.lalaland.core.utils.tuples.Pair;
import java.util.EnumMap;
import java.util.UUID;
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

  protected CombatStatHolder(final LivingEntity bukkitEntity) {
    this.bukkitEntity = bukkitEntity;
    entityID = bukkitEntity.getUniqueId();
    human = bukkitEntity instanceof Player;
    if (human) {
      ((Player) bukkitEntity).setHealthScale(20D);
    }
    combatStatMappings = Maps.newEnumMap(CombatStat.getEmptyMap());
    baseValues = Maps.newEnumMap(CombatStat.getBaseMap(human));
    recalculatingSheduled = false;
  }

  @Setter(AccessLevel.PROTECTED)
  @Getter(AccessLevel.PROTECTED)
  private boolean recalculatingSheduled;
  @Getter(AccessLevel.PROTECTED)
  private final LivingEntity bukkitEntity;
  @Getter
  private final boolean human;
  private final UUID entityID;
  private final EnumMap<CombatStat, Double> combatStatMappings;
  private final EnumMap<CombatStat, Double> baseValues;

  /**
   * Gets the complete value of a stat.
   *
   * @param stat the stat type
   * @return
   */
  public double getStatValue(final CombatStat stat) {
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

  /**
   * This will return a Pair of two Maps. The first map includes a copy of the base values of this holder. The second map includes a copy of
   * the complete calculated combat map.
   * <p>
   * Those Maps do not have any reflective impact on the base maps.
   *
   * @return a pair of two ImmutableMaps
   */
  public Pair<ImmutableMap<CombatStat, Double>, ImmutableMap<CombatStat, Double>> getValueMappings() {
    return Pair
        .of(ImmutableMap.copyOf(baseValues), ImmutableMap.copyOf(combatStatMappings));
  }

  /**
   * Rebases a new value of a stat.
   *
   * @param stat  the stat
   * @param value the value
   * @return the old value.
   */
  protected double setValue(final CombatStat stat, final double value) {
    return combatStatMappings
        .put(stat, getStatBaseValue(stat) + value);
  }

  /**
   * Resets the stat map with base value imports.
   */
  protected void resetStatMap() {
    for (final CombatStat stat : CombatStat.values()) {
      combatStatMappings.put(stat, getStatBaseValue(stat));
    }
  }

}