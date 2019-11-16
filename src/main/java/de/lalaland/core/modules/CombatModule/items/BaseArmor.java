package de.lalaland.core.modules.CombatModule.items;

import lombok.AllArgsConstructor;

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
public enum BaseArmor {

  CLOTH_HELMET("Stoff Helm"),
  CLOTH_CHESTPLATE("Stoff Brustharnisch"),
  CLOTH_LEGGINS("Stoff Beinschienen"),
  CLOTH_BOOTS("Stoff Stiefel");

  private final String displayName;

}
