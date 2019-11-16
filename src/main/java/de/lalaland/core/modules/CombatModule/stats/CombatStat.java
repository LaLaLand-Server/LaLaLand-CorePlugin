package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.lalaland.core.utils.items.ItemBuilder;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

  HEALTH("Lebenspunkte", 100, 1, 2E6D,
      false, 8000, Lists.newArrayList("§7Erhöht deine Trefferpunkte.")),
  MEELE_DAMAGE("Nahkampfschaden", 5, 0.5, 2E6D,
      false, 8001, Lists.newArrayList("§7Erhöht deinen Schaden mit", "§7allen Nahkampfwaffen.")),
  RANGE_DAMAGE("Fernkampfschaden", 4, 0.5, 2E6D,
      false, 8002, Lists.newArrayList("§7Erhöht deinen Schaden mit","§7allen Fernkampfwaffen.")),
  ATTACK_SPEED("Angriffsgeschwindigkeit", 50, 1, 500,
      false, 8003, Lists.newArrayList("§7Legt die Geschwindigkeit zwischen", "§7Angriffen fest.")),
  CRIT_CHANCE("Kritische Trefferchance", 0.0D, 0.0D, 100.0D,
      true, 8004, Lists.newArrayList("§7Erhöht deine Chance auf Kritische Treffer.")),
  CRIT_DAMAGE("Kritischer Zusatzschaden", 50.0D, 25.0D, 500D,
      true, 8005, Lists.newArrayList("§7Legt den zusätzlichen Schaden von", "§7kritischen Treffern fest.")),
  PHYSICAL_ARMOR("Verteidigung", 20, 0D, 2E6D,
      false, 8006, Lists.newArrayList("§7Verringert physikalischen Schaden", "§7durch z.B. Waffenangriffe.")),
  MYSTIC_ARMOR("Mystischer Widerstand", 10, 0D, 2E6D,
      false, 8007, Lists.newArrayList("§7Verringert den Schaden durch mystische", "§7Quellen wie z.B. ...")),
  BIO_ARMOR("Bio Widerstand", 10, 0D, 2E6D,
      false, 8008, Lists.newArrayList("&7Verringert den Schaden durch", "§7Biologische Angriffe wie", "§7Gift oder Radioaktivität."));

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
  @Getter
  private final int modelID;
  @Getter
  private final List<String> description;

  /**
   * Gets the base ItemStack for display.
   *
   * @return the icon
   */
  public ItemStack getBaseIcon() {
    return new ItemBuilder(Material.STICK).name(this.displayName).modelData(this.modelID).build();
  }

  /**
   * Gets the base value of this stat.
   *
   * @param forHuman if base value is for human entity.
   * @return the base value.
   */
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

  /**
   * Creates an empty EnumMap
   *
   * @return
   */
  public static Map<CombatStat, Double> getEmptyMap() {
    Map<CombatStat, Double> map = Maps.newHashMap();
    for (CombatStat stat : CombatStat.values()) {
      map.put(stat, 0D);
    }
    return map;
  }

}
