package de.lalaland.core.modules.combat.stats;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import de.lalaland.core.modules.combat.items.StatItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatCalculationListener implements Listener {

  public CombatCalculationListener(final CombatStatManager manager) {
    combatStatManager = manager;
  }

  private final CombatStatManager combatStatManager;

  @EventHandler
  public void onItemDrop(final PlayerDropItemEvent event) {
    final StatItem statItem = StatItem.of(event.getItemDrop().getItemStack());
    if (statItem.isCombatStatComponent()) {
      combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
    }
  }

  @EventHandler
  public void onItemDrop(final EntityDropItemEvent event) {
    final StatItem statItem = StatItem.of(event.getItemDrop().getItemStack());
    if (statItem.isCombatStatComponent()) {
      combatStatManager.recalculateValues(event.getEntity().getUniqueId());
    }
  }


  @EventHandler
  public void onItemPickup(final EntityPickupItemEvent event) {
    final StatItem statItem = StatItem.of(event.getItem().getItemStack());
    if (statItem.isCombatStatComponent()) {
      combatStatManager.recalculateValues(event.getEntity().getUniqueId());
    }
  }

  @EventHandler
  public void onItemHeldChange(final PlayerItemHeldEvent event) {
    combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onArmorChange(final PlayerArmorChangeEvent event) {
    combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onSwapHandItems(final PlayerSwapHandItemsEvent event) {
    combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onChangeMainHand(final PlayerChangedMainHandEvent event) {
    combatStatManager.recalculateValues(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    combatStatManager.initEntity(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    combatStatManager.terminateEntity(event.getPlayer());
  }

  @EventHandler
  public void onEntitySpawn(final EntitySpawnEvent event) {
    final Entity entity = event.getEntity();
    if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.ARMOR_STAND) {
      return;
    }
    combatStatManager.initEntity((LivingEntity) entity);
  }

  @EventHandler
  public void onEntityDeath(final EntityDeathEvent event) {
    combatStatManager.terminateEntity(event.getEntity());
  }

  @EventHandler
  public void onChunkUnload(final ChunkUnloadEvent event) {
    for (final Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof LivingEntity) {
        combatStatManager.terminateEntity((LivingEntity) entity);
      }
    }
  }

  @EventHandler
  public void onChunkLoad(final ChunkLoadEvent event) {
    for (final Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof LivingEntity) {
        combatStatManager.initEntity((LivingEntity) entity);
      }
    }
  }

}
