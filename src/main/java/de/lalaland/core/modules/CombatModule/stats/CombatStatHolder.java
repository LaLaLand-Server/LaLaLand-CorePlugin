package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.lalaland.core.utils.tuples.Pair;
import java.util.EnumMap;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
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

  protected CombatStatHolder(LivingEntity bukkitEntity) {
    this.bukkitEntity = bukkitEntity;
    this.entityID = bukkitEntity.getUniqueId();
    this.human = bukkitEntity instanceof Player;
    this.combatStatMappings = Maps.newEnumMap(CombatStat.getBaseMap(human));
    this.baseValues = Maps.newEnumMap(CombatStat.class);
  }

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
  public double getStatValue(CombatStat stat) {
    return combatStatMappings.get(stat);
  }

  /**
   * Gets only the base value of a stat.
   *
   * @param stat
   * @return
   */
  public double getStatBaseValue(CombatStat stat) {
    return this.baseValues.getOrDefault(stat, 0D);
  }

  /**
   * Sets the base value of a stat.
   *
   * @param stat
   * @param value
   */
  public void setStatBaseValue(CombatStat stat, double value) {
    this.baseValues.put(stat, value);
  }

  /**
   * This will return a Pair of two Maps.
   * The first map includes a copy of the base values of this holder.
   * The second map includes a copy of the complete calculated combat map.
   *
   *  Those Maps do not have any reflective impact on the base maps.
   *
   * @return a pair of two ImmutableMaps
   */
  public Pair<ImmutableMap<CombatStat, Double>, ImmutableMap<CombatStat, Double>> getValueMappings() {
    return Pair
        .of(ImmutableMap.copyOf(this.baseValues), ImmutableMap.copyOf(this.combatStatMappings));
  }

  /**
   * Rebases a new value of a stat.
   *
   * @param stat  the stat
   * @param value the value
   * @return the old value.
   */
  protected double setValue(CombatStat stat, double value) {
    return this.combatStatMappings
        .put(stat, getStatBaseValue(stat) + stat.getBaseValue(this.human) + value);
  }

  /**
   * Resets the stat map with base value imports.
   */
  protected void resetStatMap() {
    for (CombatStat stat : CombatStat.values()) {
      this.combatStatMappings.put(stat, getStatBaseValue(stat) + stat.getBaseValue(this.human));
    }
  }

}