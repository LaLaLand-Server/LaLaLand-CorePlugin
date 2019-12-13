package de.lalaland.core.modules.combat.items;

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
public enum ItemStat {

  WEAPON_TYPE("WeaponType"),
  MAX_DURABILITY("MaxDurability"),
  DURABILITY("Durability"),
  VERSION("Version"),
  CREATION_DATE("CreationDate"),
  UNBREAKABLE("Unbreakable"),
  CREATOR("Creator"),
  RETAIL_PRICE("RetailPrice");

  public static String COMPOUND_KEY = "ItemStats";

  @Getter
  private final String nbtKey;
}
