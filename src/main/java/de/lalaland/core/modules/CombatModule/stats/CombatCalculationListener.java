package de.lalaland.core.modules.CombatModule.stats;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

  public CombatCalculationListener(CombatStatManager manager) {
    this.combatStatManager = manager;
  }

  private final CombatStatManager combatStatManager;

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    combatStatManager.initEntity(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    combatStatManager.terminateEntity(event.getPlayer());
  }

  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.ARMOR_STAND) {
      return;
    }
    this.combatStatManager.initEntity((LivingEntity) entity);
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    this.combatStatManager.terminateEntity(event.getEntity());
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof LivingEntity) {
        this.combatStatManager.terminateEntity((LivingEntity) entity);
      }
    }
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof LivingEntity) {
        this.combatStatManager.initEntity((LivingEntity) entity);
      }
    }
  }

}
