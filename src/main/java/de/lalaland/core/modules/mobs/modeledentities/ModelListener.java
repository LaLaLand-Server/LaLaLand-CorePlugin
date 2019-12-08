package de.lalaland.core.modules.mobs.modeledentities;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.tasks.TaskManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ModelListener implements Listener {

  public ModelListener(final CorePlugin plugin, final MobModelManager mobModelManager) {
    taskManager = plugin.getTaskManager();
    this.mobModelManager = mobModelManager;
  }

  private final TaskManager taskManager;
  private final MobModelManager mobModelManager;

  @EventHandler
  public void onDamaged(final EntityDamageEvent event) {
    final ComplexModel<?> model = mobModelManager.getModel(event.getEntity().getUniqueId());
    if (model == null) {
      return;
    }
    model.equipHurt();
    taskManager.runBukkitSyncDelayed(() -> {
      if (!model.getBukkit().isDead()) {
        model.equipNormal();
      }
    }, 4L);
  }

  @EventHandler
  public void onAttack(final EntityDamageByEntityEvent event) {
    final ComplexModel<?> model = mobModelManager.getModel(event.getEntity().getUniqueId());
    if (model == null) {
      return;
    }
    final int attackFrames = model.getAttackFrames();
    if (attackFrames < 1) {
      return;
    }
    model.equipAttack();
    taskManager.runBukkitSyncDelayed(() -> {
      if (!model.getBukkit().isDead()) {
        model.equipNormal();
      }
    }, attackFrames);
  }

  @EventHandler
  public void onDeath(final EntityDeathEvent event) {
    mobModelManager.removeModel(event.getEntity().getUniqueId());
  }

  @EventHandler
  public void onUnload(final ChunkUnloadEvent event) {
    for (final Entity entity : event.getChunk().getEntities()) {
      mobModelManager.removeModel(entity.getUniqueId());
    }
  }

}
