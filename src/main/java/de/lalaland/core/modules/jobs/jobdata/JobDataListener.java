package de.lalaland.core.modules.jobs.jobdata;

import de.lalaland.core.tasks.TaskManager;
import java.io.IOException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobDataListener implements Listener {

  public JobDataListener(final JobDataManager jobDataManager, final TaskManager taskManager) {
    this.jobDataManager = jobDataManager;
    this.taskManager = taskManager;
  }

  private final JobDataManager jobDataManager;
  private final TaskManager taskManager;

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    try {
      jobDataManager.loadUser(event.getPlayer().getUniqueId());
    } catch (final IOException e) {
      taskManager.runBukkitSync(() -> event.getPlayer().kickPlayer("Fehler beim Laden deiner Job Daten."));
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onLeave(final PlayerQuitEvent event) {
    try {
      jobDataManager.saveUser(event.getPlayer().getUniqueId());
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
