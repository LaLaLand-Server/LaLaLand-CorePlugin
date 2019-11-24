package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import java.util.Set;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class StructureListener implements Listener {

  public StructureListener(final RegionManager regionManager,
      final StructureManager structureManager) {
    this.regionManager = regionManager;
    this.structureManager = structureManager;
  }

  private final RegionManager regionManager;
  private final StructureManager structureManager;

  // TODO maybe fire RegionBreakEvent and use instead of calling region here and in RegionListener
  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final Set<ProtectedRegion> regions = regionManager.getRegionsAt(event.getBlock().getLocation());
    for (final ProtectedRegion region : regions) {
      final Structure struct = structureManager.getStructure(region);
      if (struct != null) {
        struct.onBlockBreak(event);
      }
    }
  }

  // TODO maybe fire RegionInteractEvent and use instead of calling region here and in RegionListener
  @EventHandler
  public void onBlockInteract(final PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    final Set<ProtectedRegion> regions = regionManager
        .getRegionsAt(event.getClickedBlock().getLocation());
    for (final ProtectedRegion region : regions) {
      final Structure struct = structureManager.getStructure(region);
      if (struct != null) {
        struct.onInteract(event);
      }
    }
  }

}
