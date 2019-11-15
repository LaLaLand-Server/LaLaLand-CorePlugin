package de.lalaland.core.user.task;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.User;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 14.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class SaveUserDataThread implements Runnable {

  private final CorePlugin corePlugin;

  /**
   * Instantiates a new Remove offline user thread. Remove User classes stored in cache when they
   * are not in use.
   *
   * @param corePlugin the core plugin
   */
  public SaveUserDataThread(final CorePlugin corePlugin) {
    this.corePlugin = corePlugin;
  }

  @Override
  public void run() {
    corePlugin.getTaskManager().runBukkitSync(() ->{
      for (final User user : corePlugin.getUserManager()) {
        if (user.isUpdateCandidate()) {
          user.save();
        }
      }
    });
  }


}
