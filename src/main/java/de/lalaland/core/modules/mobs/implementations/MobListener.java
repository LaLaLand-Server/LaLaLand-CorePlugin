package de.lalaland.core.modules.mobs.implementations;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MobListener implements Listener {

  @EventHandler
  public void onDeath(final EntityDeathEvent event) {
    if (event.getEntity().getScoreboardTags().contains("TOKEN_HOLDER")) {
      for (final Entity passenger : event.getEntity().getPassengers()) {
        if (passenger instanceof ArmorStand) {
          passenger.remove();
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onUnload(final EntityDeathEvent event) {
    if (event.getEntity().getScoreboardTags().contains("TOKEN_HOLDER")) {
      for (final Entity passenger : event.getEntity().getPassengers()) {
        if (passenger instanceof ArmorStand) {
          passenger.remove();
          event.getEntity().remove();
        }
      }
    }
  }

}
