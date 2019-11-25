package de.lalaland.core.modules.resourcepack.distribution;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.tasks.TaskManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourcepackListener implements Listener {

  public ResourcepackListener(final CorePlugin plugin) {
    taskManager = plugin.getTaskManager();
  }

  private final TaskManager taskManager;

  @EventHandler(priority = EventPriority.HIGH)
  public void onJoin(final PlayerJoinEvent event) {
    System.out.println("Setting pack");
    final Player player = event.getPlayer();
    taskManager.runBukkitSyncDelayed(() ->{
      player
          .setResourcePack("http://" + ResourcepackManager.host + ":" + ResourcepackManager.port + "/serverpack.zip", ResourcepackManager.hash);
    }, 20L);


  }

}
