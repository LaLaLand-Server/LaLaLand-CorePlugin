package de.lalaland.core.modules.loot.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 31.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DropProtectionListener implements Listener {

  @EventHandler
  public void onPickup(EntityPickupItemEvent event) {
    if (!DropProtection.canPickup(event.getItem(), event.getEntity().getUniqueId())) {
      event.setCancelled(true);
    }
  }

}
