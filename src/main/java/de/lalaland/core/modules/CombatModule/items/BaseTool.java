package de.lalaland.core.modules.CombatModule.items;

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
public enum BaseTool {

  WOODEN_PICKAXE("Holz Spitzhacke"),
  WOODEN_WOODCUTTING_AXE("Holz Axt"),
  WOODEN_SPADE("Holz Spaten");

  @Getter
  private final String displayName;

}
