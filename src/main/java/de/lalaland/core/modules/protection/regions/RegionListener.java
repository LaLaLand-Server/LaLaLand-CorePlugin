package de.lalaland.core.modules.protection.regions;


import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.modules.combat.stats.CombatStatManager;
import de.lalaland.core.modules.protection.zones.WorldZone;
import de.lalaland.core.modules.protection.zones.WorldZoneManager;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.user.data.UserData;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.bukkit.event.player.PlayerMoveEvent;
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

  public RegionListener(final RegionManager regionManager, final CorePlugin plugin, final WorldZoneManager worldZoneManager) {
    userManager = plugin.getUserManager();
    this.regionManager = regionManager;
    this.worldZoneManager = worldZoneManager;
    combatStatManager = plugin.getModule(CombatModule.class).getCombatStatManager();
  }

  private final WorldZoneManager worldZoneManager;
  private final RegionManager regionManager;
  private final UserManager userManager;
  private final CombatStatManager combatStatManager;

  @EventHandler
  public void onMove(final PlayerMoveEvent event) {
    final Location toLoc = event.getTo();
    final Location fromLoc = event.getTo();
    if (fromLoc.getBlockX() == toLoc.getBlockX() || fromLoc.getBlockZ() == toLoc.getBlockX()) {
      return;
    }
    final ProtectedRegion fromRegion = regionManager.getMostRelevantRegion(toLoc);
    final ProtectedRegion toRegion = regionManager.getMostRelevantRegion(toLoc);
    if (fromRegion.equals(toRegion)) {
      return;
    }
    final WorldZone zone = worldZoneManager.getZoneOf(toRegion);
    if (zone == null) {
      return;
    }

    final UserData data = userManager.getUser(event.getPlayer().getUniqueId()).getUserData();

    if (data.hasDiscovered(zone)) {
      // TODO on discovery
      data.addZoneDiscovery(zone);
      event.getPlayer().sendTitle(zone.getDisplayName(), "§eErkundet", 20, 70, 20);
      // TODO rework EXP system
      data.addExp(zone.getDiscoveryExp());
    } else {
      // TODO on normal enter
      event.getPlayer().sendTitle(zone.getDisplayName(), "", 10, 30, 10);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockBreak(final BlockBreakEvent event) {
    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getBlock().getLocation());
    if (region == null) {
      return;
    }
    final Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.BLOCK_BREAK);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(final BlockPlaceEvent event) {
    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getBlock().getLocation());
    if (region == null) {
      return;
    }
    final Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.BLOCK_PLACE);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockInteract(final PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK
        && event.getAction() != Action.LEFT_CLICK_BLOCK) {
      return;
    }
    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getClickedBlock().getLocation());
    if (region == null) {
      return;
    }

    final Permit permit;

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

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityInteract(final PlayerInteractAtEntityEvent event) {

    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getRightClicked().getLocation());
    if (region == null) {
      return;
    }
    final Permit permit = region
        .getPermit(event.getPlayer().getUniqueId(), RegionRule.INTERACT_AT_ENTITY);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
      // TODO play deny effect
    }

  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onDamageEntity(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player)) {
      return;
    }

    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getEntity().getLocation());
    if (region == null) {
      return;
    }

    final Permit permit;

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

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPistonEvent(final BlockPistonExtendEvent event) {
    for (final Block block : event.getBlocks()) {
      final ProtectedRegion region = regionManager.getMostRelevantRegion(block.getLocation());
      if (region == null) {
        continue;
      }
      final Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.PISTON_EXTEND);
      if (permit == Permit.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPistonEvent(final BlockPistonRetractEvent event) {
    for (final Block block : event.getBlocks()) {
      final ProtectedRegion region = regionManager.getMostRelevantRegion(block.getLocation());
      if (region == null) {
        continue;
      }
      final Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.PISTON_EXTEND);
      if (permit == Permit.DENY) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onExplode(final EntityExplodeEvent event) {
    final ListIterator<Block> blockIter = event.blockList().listIterator();
    while (blockIter.hasNext()) {
      final Block next = blockIter.next();
      final ProtectedRegion region = regionManager.getMostRelevantRegion(next.getLocation());
      if (region != null) {
        final Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.BLOCK_EXPLODE);
        if (permit == Permit.DENY) {
          blockIter.remove();
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onExplode(final BlockExplodeEvent event) {
    final ListIterator<Block> blockIter = event.blockList().listIterator();
    while (blockIter.hasNext()) {
      final Block next = blockIter.next();
      final ProtectedRegion region = regionManager.getMostRelevantRegion(next.getLocation());
      if (region != null) {
        final Permit permit = region.getRuleSet().getPermit(Relation.NEUTRAL, RegionRule.BLOCK_EXPLODE);
        if (permit == Permit.DENY) {
          blockIter.remove();
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onItemPickup(final PlayerAttemptPickupItemEvent event) {
    final ProtectedRegion region = regionManager
        .getMostRelevantRegion(event.getPlayer().getLocation());
    if (region == null) {
      return;
    }
    final Permit permit = region.getPermit(event.getPlayer().getUniqueId(), RegionRule.ITEM_PICKUP);
    if (permit == Permit.DENY) {
      event.setCancelled(true);
    }
  }

}
