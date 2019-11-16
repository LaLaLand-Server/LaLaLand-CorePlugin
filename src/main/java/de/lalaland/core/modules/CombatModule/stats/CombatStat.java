package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum CombatStat {

  HEALTH("Lebenspunkte", 100, 1, 2E6D, false),
  MEELE_DAMAGE("Nahkampfschaden", 5, 0.5, 2E6D, false),
  RANGE_DAMAGE("Fernkampfschaden", 4, 0.5, 2E6D, false),
  CRIT_CHANCE("Kritische Trefferchance", 0.0D, 0.0D, 100.0D, true),
  CRIT_DAMAGE("Kritischer Zusatzschaden", 50.0D, 25.0D, 500D, true),
  PHYSICAL_ARMOR("Verteidigung", 20, 0D, 2E6D, false),
  MYSTIC_ARMOR("Mystischer Widerstand", 10, 0D, 2E6D, false),
  BIO_ARMOR("Bio Widerstand", 10, 0D, 2E6D, false);

  public static String COMPOUND_KEY = "CombatStats";

  @Getter
  private final String displayName;
  @Getter
  private final double playerBaseValue;
  @Getter
  private final double minValue;
  @Getter
  private final double maxValue;
  @Getter
  private final boolean percentageStyle;

  public double getBaseValue(boolean forHuman) {
    if (forHuman) {
      return this.playerBaseValue;
    } else {
      return this.minValue;
    }
  }

  /**
   * Creates an empty Map with base values.
   *
   * @param humanEntity if the map should be build for a player.
   * @return A map with the player base values or min values.
   */
  public static Map<CombatStat, Double> getBaseMap(boolean humanEntity) {
    Map<CombatStat, Double> map = Maps.newHashMap();

    if (humanEntity) {
      for (CombatStat stat : CombatStat.values()) {
        map.put(stat, stat.getPlayerBaseValue());
      }
    } else {
      for (CombatStat stat : CombatStat.values()) {
        map.put(stat, stat.minValue);
      }
    }

    return map;
  }

}
