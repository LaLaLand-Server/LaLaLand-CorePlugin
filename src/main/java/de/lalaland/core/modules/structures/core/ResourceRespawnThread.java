package de.lalaland.core.modules.structures.core;

import com.google.common.collect.Queues;
import de.lalaland.core.utils.common.BukkitTime;
import java.util.PriorityQueue;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 29.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourceRespawnThread implements Runnable {

  protected ResourceRespawnThread() {
    respawnResources = Queues.newPriorityQueue();
  }

  private final PriorityQueue<JobResource> respawnResources;

  protected void addResourceToRespawn(final JobResource resource) {
    respawnResources.add(resource);
  }

  @Override
  public void run() {
    if (respawnResources.isEmpty()) {
      return;
    }

    while (!BukkitTime.isMsElapsed(10)) {

      final JobResource resource = respawnResources.peek();
      if (resource.timeToNextRespawn() <= 0) {
        resource.buildBlocks();
        resource.setDepleted(false);
        resource.heal();
        respawnResources.remove();
      } else {
        break;
      }

      if (respawnResources.isEmpty()) {
        break;
      }

    }

  }

}