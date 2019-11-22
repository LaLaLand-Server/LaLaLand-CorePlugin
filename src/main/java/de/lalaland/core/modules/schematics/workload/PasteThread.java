package de.lalaland.core.modules.schematics.workload;

import de.lalaland.core.utils.common.BukkitTime;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PasteThread implements Runnable {

  private static final long MAX_PASTE_TIME_MILLIS = 10L;

  public PasteThread() {
    pasteQueue = new ObjectArrayFIFOQueue<>();
  }

  private final ObjectArrayFIFOQueue<PasteJob> pasteQueue;

  public void add(final PasteJob job) {
    pasteQueue.enqueue(job);
  }

  @Override
  public void run() {
    while (!BukkitTime.isMsElapsed(MAX_PASTE_TIME_MILLIS)) {
      if (pasteQueue.isEmpty()) {
        break;
      } else {
        pasteQueue.dequeue().paste();
      }
    }
  }

}
