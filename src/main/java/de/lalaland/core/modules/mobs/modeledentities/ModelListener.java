package de.lalaland.core.modules.mobs.modeledentities;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.IBiPiped;
import de.lalaland.core.tasks.TaskManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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
    final IBiPiped model = mobModelManager.getBiModel(event.getEntity().getUniqueId());
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

}
