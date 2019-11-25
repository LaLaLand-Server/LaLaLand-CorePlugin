package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.schematics.core.Schematic;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Location;

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


  public void deleteStructure(final Structure structure) {
    structure.destroyBlocks();
    unlinkStructure(structure);
  }

  protected void unlinkStructure(final Structure struct) {
    final ProtectedRegion region = struct.getProtectedRegion();
    structureRegionMap.remove(region);
    regionManager.removeRegion(region);
  }

  public BaseStructure createBaseStructure(final Schematic baseSchematic, final Location groundCenter) {
    final ProtectedRegion schematicRegion = regionManager.createFor(baseSchematic, groundCenter);
    final BaseStructure structure = new BaseStructure(schematicRegion, baseSchematic, this);
    structureRegionMap.put(schematicRegion, structure);
    return structure;
  }

}
