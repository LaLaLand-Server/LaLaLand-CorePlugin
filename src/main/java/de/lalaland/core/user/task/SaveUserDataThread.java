package de.lalaland.core.user.task;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.User;
import de.lalaland.core.user.UserManager;

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

  /**
   * Instantiates a new Remove offline user thread. Remove User classes stored in cache when they are not in use.
   *
   * @param corePlugin the core plugin
   */
  public SaveUserDataThread(final CorePlugin corePlugin) {
    userManager = corePlugin.getUserManager();
  }

  private final UserManager userManager;

  @Override
  public void run() {

    for (final User user : userManager) {
      if (user.isUpdateCandidate()) {
        user.save();
      }
    }

  }


}
