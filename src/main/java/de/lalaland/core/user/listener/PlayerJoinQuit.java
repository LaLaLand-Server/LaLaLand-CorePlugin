package de.lalaland.core.user.listener;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.UserManager;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class PlayerJoinQuit implements Listener {

  private final CorePlugin plugin;

  public PlayerJoinQuit(final CorePlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onJoin(final PlayerJoinEvent playerJoinEvent) {
    final UUID playerID = playerJoinEvent.getPlayer().getUniqueId();
    final UserManager userManager = plugin.getUserManager();
    if (userManager.isCached(playerID)) {
      userManager.getUser(playerID).setOnline();
    } else {
      userManager.addUserToCache(playerID);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent playerQuitEvent) {
    final Player player = playerQuitEvent.getPlayer();
    plugin.getUserManager().getUser(player.getUniqueId()).setOffline();
  }

}