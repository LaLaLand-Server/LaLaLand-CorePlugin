package de.lalaland.core.modules.protection.regions;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 18.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectedRegion {

  protected ProtectedRegion(BoundingBox region, UUID regionID) {
    this.regionID = regionID;
    this.boundingBox = region;
  }

  private final BoundingBox boundingBox;
  @Getter
  private final UUID regionID;
  @Getter @Setter
  private int priority;

  public boolean contains(Vector vector) {
    return this.boundingBox.contains(vector);
  }

  public boolean contains(Location location) {
    return this.contains(location.toVector());
  }

  public boolean contains(Block block) {
    return this.contains(block.getLocation());
  }

}
