package de.lalaland.core.modules.structures.core;

import com.google.gson.JsonObject;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.schematics.core.RelativeBlockData;
import de.lalaland.core.modules.schematics.core.Schematic;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BaseStructure extends Structure {

  public BaseStructure(final ProtectedRegion protectedRegion, final Schematic baseSchematic, final StructureManager structureManager) {
    super(protectedRegion, baseSchematic, structureManager);
    buildBlocks();
    for (final RelativeBlockData data : baseSchematic.getBlockDataClone()) {
      final Material mat = data.getBlockData().getMaterial();
      if (mat != Material.AIR) {
        addValidMaterial(mat);
      }
    }
  }

  @Override
  public void onBlockBreak(final BlockBreakEvent event) {
    structureManager.deleteStructure(this);
  }

  @Override
  public void onInteract(final PlayerInteractEvent event) {

  }

  @Override
  public JsonObject asJsonObject() {
    return null;
  }

}