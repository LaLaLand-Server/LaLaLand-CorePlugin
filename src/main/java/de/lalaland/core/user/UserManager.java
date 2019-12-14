package de.lalaland.core.user;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.tasks.TaskManager;
import de.lalaland.core.user.listener.PlayerJoinQuit;
import de.lalaland.core.user.task.RemoveOfflineUserThread;
import de.lalaland.core.user.task.SaveUserDataThread;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UserManager implements Iterable<User> {

  private final CorePlugin corePlugin;
  private final Object2ObjectOpenHashMap<UUID, User> cachedUsers;

  /**
   * Instantiates a new User manager.
   *
   * @param corePlugin the core plugin
   */
  public UserManager(final CorePlugin corePlugin) {
    this.corePlugin = corePlugin;
    cachedUsers = new Object2ObjectOpenHashMap<>();
    Bukkit.getPluginManager().registerEvents(new PlayerJoinQuit(corePlugin), corePlugin);
    addAllOnlinePlayerToCache(); // in case of reload
    executeTasks();
  }

  /**
   * Loads all current online players.
   */
  private void addAllOnlinePlayerToCache() {
    Bukkit.getOnlinePlayers().forEach(player -> addUserToCache(player.getUniqueId()));
  }

  /**
   * Starts Threads that cleans offline users from cachedUsers collection.
   */
  private void executeTasks() {
    final TaskManager taskManager = corePlugin.getTaskManager();
    int interval;

    interval = corePlugin.getCoreConfig().getUnusedUserRemoverInterval();
    final RemoveOfflineUserThread removeUserThread = new RemoveOfflineUserThread(corePlugin);
    taskManager.executeScheduledTask(removeUserThread, 0L, interval, TimeUnit.MINUTES);

    interval = corePlugin.getCoreConfig().getUserSaveInterval();
    final SaveUserDataThread saveUserThread = new SaveUserDataThread(corePlugin);
    taskManager.executeScheduledTask(saveUserThread, 0L, interval, TimeUnit.MINUTES);
  }

  /**
   * Add user to the cache.
   *
   * @param uuid the uuid
   */
  public void addUserToCache(final UUID uuid) {

    if (isCached(uuid)) {
      return;
    }

    cachedUsers.put(uuid, new User(corePlugin, uuid));
  }

  /**
   * Remove user from the cache.
   *
   * @param uuid the uuid
   */
  public void removeUserFromCache(final UUID uuid) {

    if (!isCached(uuid)) {
      return;
    }

    getUser(uuid).save();
    cachedUsers.remove(uuid);
  }

  /**
   * Gets user.
   *
   * @param uuid the uuid of the player
   * @return the user class of the player
   */
  public User getUser(final UUID uuid) {

    if (!isCached(uuid)) {
      addUserToCache(uuid);
    }

    return cachedUsers.get(uuid);
  }

  public boolean isCached(final UUID uuid) {
    return cachedUsers.containsKey(uuid);
  }

  @Override
  public Iterator<User> iterator() {
    return cachedUsers.values().iterator();
  }

}
