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
public enum WeaponType {

  SWORD("Schwert", true),
  AXE("Axt", true),
  DAGGER("Dolch", true),
  MACE("Streitkolben", true),
  SHORT_BOW("Kurzbogen", false),
  LONG_BOW("Langbogen", false),
  CROSS_BOW("Armbrust", false),
  STAFF("Stab", false),
  HELMET("Helm", false),
  CHESTPLATE("Brustharnisch", false),
  LEGGINS("Beinschienen", false),
  BOOTS("Stiefel", false);

  @Getter
  private final String displayName;
  @Getter
  private final boolean meele;

}
