package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.UUID;
import javax.annotation.Nullable;
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

  protected CombatStatHolder(LivingEntity bukkitEntity, boolean isHumanEntity) {
    this.bukkitEntity = bukkitEntity;
    this.entityID = bukkitEntity.getUniqueId();
    this.combatStatMappings = Maps.newEnumMap(CombatStat.getBaseMap(isHumanEntity));
    this.baseValues = Maps.newEnumMap(CombatStat.class);
    this.human = bukkitEntity instanceof Player;
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

  public double getStatBaseValue(CombatStat stat) {
    return this.baseValues.getOrDefault(stat, 0D);
  }

  public void setBaseValue(CombatStat stat, double value) {
    this.baseValues.put(stat, value);
  }

  /**
   * Rebases a new value for of a stat.
   *
   * @param stat  the stat
   * @param value the value
   * @return the old value.
   */
  protected double setValue(CombatStat stat, double value) {
    return this.combatStatMappings.put(stat, stat.getBaseValue(this.human) + value);
  }

  protected void resetMap() {
    for (CombatStat stat : CombatStat.values()) {
      this.combatStatMappings.put(stat, stat.getBaseValue(this.human));
    }
  }

}