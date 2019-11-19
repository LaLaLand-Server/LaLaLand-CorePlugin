package de.lalaland.core.modules.protection.regions;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RegionListener implements Listener {

  public RegionListener(RegionManager regionManager) {
    this.regionManager = regionManager;
  }

  private final RegionManager regionManager;

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getBlock().getLocation());
    if (region == null) {
      return;
    }
    Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.BLOCK_BREAK);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getBlock().getLocation());
    if (region == null) {
      return;
    }
    Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.BLOCK_PLACE);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }
  }

  @EventHandler
  public void onBlockInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK
        && event.getAction() != Action.LEFT_CLICK_BLOCK) {
      return;
    }
    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getClickedBlock().getLocation());
    if (region == null) {
      return;
    }
    Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.RIGHT_CLICK_BLOCK);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }

  }

}
