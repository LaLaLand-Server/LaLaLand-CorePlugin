package de.lalaland.core.utils.actionbar;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarUpdateThread implements Runnable {

  protected static final long UPDATE_PERIOD = 10L;

  public ActionBarUpdateThread(final ActionBarManager actionBarManager) {
    this.actionBarManager = actionBarManager;
  }

  private final ActionBarManager actionBarManager;

  @Override
  public void run() {
    actionBarManager.updateAndShowAll();
  }

}
