package de.lalaland.core.user.task;

import com.google.common.collect.Sets;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.User;
import de.lalaland.core.user.UserManager;
import java.util.Set;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 14.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RemoveOfflineUserThread implements Runnable {

  private final CorePlugin corePlugin;

  /**
   * Instantiates a new Remove offline user thread. Remove User classes stored in cache when they
   * are not in use.
   *
   * @param corePlugin the core plugin
   */
  public RemoveOfflineUserThread(final CorePlugin corePlugin) {
    this.corePlugin = corePlugin;
  }

  @Override
  public void run() {

    corePlugin.getTaskManager().runBukkitSync(() -> {
      final UserManager userManager = corePlugin.getUserManager();
      final Set<User> removeCandidates = Sets.newHashSet();

      for (final User user : userManager) {
        if (!user.getOnlinePlayer().isPresent()) {
          removeCandidates.add(user);
        }
      }

      for (final User offlineUser : removeCandidates) {
        userManager.removeUserFromCache(offlineUser.getUuid());
      }
    });

  }

}
