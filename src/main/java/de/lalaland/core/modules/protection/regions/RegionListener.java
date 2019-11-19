package de.lalaland.core.modules.protection.regions;


import java.util.ListIterator;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

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

    Permit permit;

    if (event.getClickedBlock().getState() instanceof InventoryHolder) {
      permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.OPEN_INVENTORY_BLOCK);
    } else {
      permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.RIGHT_CLICK_BLOCK);
    }
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }

  }

  @EventHandler
  public void onEntityInteract(PlayerInteractAtEntityEvent event) {

    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getRightClicked().getLocation());
    if (region == null) {
      return;
    }
    Permit permit = region
        .getPermit(event.getPlayer().getUniqueId(), RegionRule.INTERACT_AT_ENTITY);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }

  }

  @EventHandler
  public void onDamageEntity(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player)) {
      return;
    }

    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getEntity().getLocation());
    if (region == null) {
      return;
    }

    Permit permit;

    if (!(event.getEntity() instanceof Player)) {
      permit = region
          .getPermit(event.getDamager().getUniqueId(), RegionRule.DAMAGE_PLAYER);
    } else if (event.getEntity().getType() == EntityType.ARMOR_STAND) {
      permit = region
          .getPermit(event.getDamager().getUniqueId(), RegionRule.DAMAGE_ARMORSTAND);
    } else {
      permit = region
          .getPermit(event.getDamager().getUniqueId(), RegionRule.INTERACT_AT_ENTITY);
    }

    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }

  }

  @EventHandler
  public void onPistonEvent(BlockPistonExtendEvent event) {
    for (Block block : event.getBlocks()) {
      ProtectedRegion region = this.regionManager.getMostRelevantRegion(block.getLocation());
      if (region == null) {
        continue;
      }
      Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.PISTON_EXTEND);
      if (permit == Permit.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler
  public void onPistonEvent(BlockPistonRetractEvent event) {
    for (Block block : event.getBlocks()) {
      ProtectedRegion region = this.regionManager.getMostRelevantRegion(block.getLocation());
      if (region == null) {
        continue;
      }
      Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.PISTON_EXTEND);
      if (permit == Permit.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent event) {
    ListIterator<Block> blockIter = event.blockList().listIterator();
    while (blockIter.hasNext()) {
      Block next = blockIter.next();
      ProtectedRegion region = this.regionManager.getMostRelevantRegion(next.getLocation());
      if (region != null) {
        Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.BLOCK_EXPLODE);
        if (permit == Permit.DENY) {
          blockIter.remove();
        }
      }
    }
  }

  @EventHandler
  public void onExplode(BlockExplodeEvent event) {
    ListIterator<Block> blockIter = event.blockList().listIterator();
    while (blockIter.hasNext()) {
      Block next = blockIter.next();
      ProtectedRegion region = this.regionManager.getMostRelevantRegion(next.getLocation());
      if (region != null) {
        Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.BLOCK_EXPLODE);
        if (permit == Permit.DENY) {
          blockIter.remove();
        }
      }
    }
  }

  @EventHandler
  public void onItemPickup(PlayerAttemptPickupItemEvent event) {
    ProtectedRegion region = this.regionManager
        .getMostRelevantRegion(event.getPlayer().getLocation());
    if (region == null) {
      return;
    }
    Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.ITEM_PICKUP);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
    }
  }

}
