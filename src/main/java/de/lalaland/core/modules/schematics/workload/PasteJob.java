package de.lalaland.core.modules.schematics.workload;

import lombok.Data;
import org.bukkit.Location;
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
@Data
public class PasteJob {

  private final Location location;
  private final BlockData blockData;

  public void paste() {
    location.getBlock().setBlockData(blockData);
  }

}
