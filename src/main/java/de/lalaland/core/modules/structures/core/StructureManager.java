package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class StructureManager {

  public StructureManager(final RegionManager regionManager) {
    this.regionManager = regionManager;
    structureRegionMap = new Object2ObjectOpenHashMap<>();
  }

  private final RegionManager regionManager;

  private final Object2ObjectOpenHashMap<ProtectedRegion, Structure> structureRegionMap;

  public Structure getStructure(final ProtectedRegion region) {
    return structureRegionMap.get(region);
  }

}
