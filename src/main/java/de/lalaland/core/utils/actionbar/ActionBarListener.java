package de.lalaland.core.utils.actionbar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarListener implements Listener {

  public ActionBarListener(final ActionBarManager actionBarManager) {
    this.actionBarManager = actionBarManager;
  }

  private final ActionBarManager actionBarManager;

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    actionBarManager.init(event.getPlayer());
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    actionBarManager.terminate(event.getPlayer());
  }

}
