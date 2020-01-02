package de.lalaland.core.modules.resourcepack.skins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 03.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum ModelBlock {

  COPPER_ORE_BLOCK(Material.ORANGE_GLAZED_TERRACOTTA,"facing=north"),
  TIN_ORE_BLOCK(Material.GRAY_GLAZED_TERRACOTTA,"facing=north"),
  IRON_ORE_BLOCK(Material.RED_GLAZED_TERRACOTTA,"facing=north"),
  STILLIT_ORE_BLOCK(Material.YELLOW_GLAZED_TERRACOTTA,"facing=north");

  @Getter
  private final Material baseMaterial;
  @Getter
  private final String blockStateApplicant;

}