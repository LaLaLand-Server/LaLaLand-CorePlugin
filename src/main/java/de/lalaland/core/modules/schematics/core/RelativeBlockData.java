package de.lalaland.core.modules.schematics.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RelativeBlockData {

  private static final String SPACING = "#";

  public RelativeBlockData(final int x, final int y, final int z, final BlockData blockData) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockData = blockData;
  }

  public RelativeBlockData(final String serialString) {
    final String[] split = serialString.split(SPACING);
    x = Integer.parseInt(split[0]);
    y = Integer.parseInt(split[1]);
    z = Integer.parseInt(split[2]);
    blockData = Bukkit.createBlockData(split[3]);
  }

  protected final int x;
  protected final int y;
  protected final int z;
  @Getter
  protected final BlockData blockData;

  public String getAsString() {
    return (x + SPACING + y + SPACING + z + SPACING + blockData.getAsString());
  }

}