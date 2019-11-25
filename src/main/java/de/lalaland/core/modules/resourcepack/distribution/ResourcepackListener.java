package de.lalaland.core.modules.resourcepack.distribution;

import com.google.common.collect.Sets;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.tasks.TaskManager;
import java.util.HashSet;
import java.util.UUID;
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

  public ResourcepackListener(final CorePlugin plugin, final ResourcepackManager manager) {
    this.manager = manager;
    this.plugin = plugin;
    attempts = Sets.newHashSet();
    taskManager = plugin.getTaskManager();
  }

  private final ResourcepackManager manager;
  private final CorePlugin plugin;
  private final TaskManager taskManager;
  private final HashSet<UUID> attempts;

  @EventHandler(priority = EventPriority.HIGH)
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    taskManager.runBukkitSyncDelayed(() -> sendResourcepack(player), 20L);
  }

//  @EventHandler
//  public void resourceStatusEvent(final PlayerResourcePackStatusEvent event) {
//    final Player player = event.getPlayer();
//    final UUID id = player.getUniqueId();
//    final Status status = event.getStatus();
//    if (status == Status.SUCCESSFULLY_LOADED) {
//      return;
//    } else if (status == Status.FAILED_DOWNLOAD) {
//      if (attempts.contains(id)) {
//        attempts.remove(id);
//        player.kickPlayer("Das Resourcepack wurde nicht akzeptiert.");
//        return;
//      } else {
//        attempts.add(id);
//        plugin.getTaskManager().runBukkitSyncDelayed(() -> sendResourcepack(player), 60L);
//      }
//
//    } else if (status == Status.DECLINED) {
//      player.kickPlayer("Das Resourcepack wurde nicht akzeptiert.");
//    }
//  }

  private void sendResourcepack(final Player player) {
    player.setResourcePack(manager.getDownloadURL("serverpack.zip"), manager.getResourceHash());
  }
}