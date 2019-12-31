package de.lalaland.core.modules.combat.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.lalaland.core.modules.resourcepack.skins.Model;
import de.lalaland.core.utils.items.ItemBuilder;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

  HEALTH("Lebenspunkte", 100, 1, 5E4D,
      false, Model.HEALTH_ICON, Lists.newArrayList("§7Erhöht deine Trefferpunkte.")),
  MANA("Mana", 100, 1, 5E4D,
      false, Model.MANA_ICON, Lists.newArrayList("§7Erhöht deine Manapunkte.")),
  MEELE_DAMAGE("Nahkampfschaden", 5, 0.5, 2E4,
      false, Model.MEELE_DAMAGE_ICON, Lists.newArrayList("§7Erhöht deinen Schaden mit", "§7allen Nahkampfwaffen.")),
  RANGE_DAMAGE("Fernkampfschaden", 4, 0.5, 2E4D,
      false, Model.RANGED_DAMAGE_ICON, Lists.newArrayList("§7Erhöht deinen Schaden mit", "§7allen Fernkampfwaffen.")),
  MIGHT("Macht", 0, 0, 2E4D,
      false, Model.MIGHT_ICON, Lists.newArrayList("§7Erhöht deinen Schaden mit", "§7magischen Angriffen.")),
  ATTACK_SPEED("Angriffsgeschwindigkeit", 100, 1, 500,
      false, Model.ATTACK_SPEED_ICON, Lists.newArrayList("§7Legt die Geschwindigkeit zwischen", "§7Angriffen fest.")),
  CRIT_CHANCE("Kritische Trefferchance", 0.0D, 0.0D, 100.0D,
      true, Model.CRIT_CHANCE_ICON, Lists.newArrayList("§7Erhöht deine Chance auf Kritische Treffer.")),
  CRIT_DAMAGE("Kritischer Zusatzschaden", 50.0D, 25.0D, 500D,
      true, Model.CRIT_DAMAGE_ICON, Lists.newArrayList("§7Legt den zusätzlichen Schaden von", "§7kritischen Treffern fest.")),
  PHYSICAL_ARMOR("Verteidigung", 20, 0D, 2E4D,
      false, Model.PHYSICAL_DAMAGE_ICON, Lists.newArrayList("§7Verringert physikalischen Schaden", "§7durch z.B. Waffenangriffe.")),
  MYSTIC_ARMOR("Mystischer Widerstand", 10, 0D, 2E4D,
      false, Model.MAGIC_ARMOR_ICON, Lists.newArrayList("§7Verringert den Schaden durch mystische", "§7Quellen wie z.B. ...")),
  BIO_ARMOR("Bio Widerstand", 10, 0D, 2E4D,
      false, Model.BIO_ARMOR_ICON,
      Lists.newArrayList("&7Verringert den Schaden durch", "§7Biologische Angriffe wie", "§7Gift oder Radioaktivität.")),
  SPEED("Lauftempo", 100, 0, 500,
      false, Model.SPEED_ICON, Lists.newArrayList("§7Erhöht dein Lauftempo."));

  public static final String COMPOUND_KEY = "CombatStats";
  protected static final double DEFENCE_HARD_CAP = 2E4;

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
  private final Model model;
  @Getter
  private final List<String> description;

  public ItemStack getIcon(CombatStatHolder combatStatHolder) {
    ItemBuilder itemBuilder = new ItemBuilder(model.getItem());

    return itemBuilder.build();
  }

  /**
   * Gets the base value of this stat.
   *
   * @param forHuman if base value is for human entity.
   * @return the base value.
   */
  public double getBaseValue(final boolean forHuman) {
    if (forHuman) {
      return playerBaseValue;
    } else {
      return minValue;
    }
  }

  /**
   * Creates an empty Map with base values.
   *
   * @param humanEntity if the map should be build for a player.
   * @return A map with the player base values or min values.
   */
  public static Map<CombatStat, Double> getBaseMap(final boolean humanEntity) {
    final Map<CombatStat, Double> map = Maps.newHashMap();

    if (humanEntity) {
      for (final CombatStat stat : CombatStat.values()) {
        map.put(stat, stat.getPlayerBaseValue());
      }
    } else {
      for (final CombatStat stat : CombatStat.values()) {
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
    final Map<CombatStat, Double> map = Maps.newHashMap();
    for (final CombatStat stat : CombatStat.values()) {
      map.put(stat, 0D);
    }
    return map;
  }

}
