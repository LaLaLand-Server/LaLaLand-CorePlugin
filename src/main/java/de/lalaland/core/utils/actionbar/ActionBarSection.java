package de.lalaland.core.utils.actionbar;

import com.google.common.collect.Queues;
import java.util.PriorityQueue;
import java.util.function.Supplier;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarSection {

  protected ActionBarSection(final ActionBarManager actionBarManager) {
    sectionLines = Queues.newPriorityQueue();
    sectionLines.add(ActionLine.empty());
    this.actionBarManager = actionBarManager;
  }

  @Getter
  private final PriorityQueue<ActionLine> sectionLines;
  private final ActionBarManager actionBarManager;

  public ActionLine addLayer(final int priority, final Supplier<String> lineSupplier) {
    final ActionLine line = new ActionLine(priority, lineSupplier);
    addLayer(line);
    return line;
  }

  public void removeLayer(final ActionLine line) {
    sectionLines.remove(line);
  }

  public void addTempLayer(final long lifeTicks, final ActionLine line) {
    sectionLines.add(line);
    actionBarManager.getTaskManager().runBukkitSyncDelayed(() -> {
      removeLayer(line);
    }, lifeTicks);
  }

  public ActionLine addTempLayer(final long lifeTicks, final int priority,
      final Supplier<String> lineSupplier) {
    final ActionLine line = new ActionLine(priority, lineSupplier);
    addTempLayer(lifeTicks, line);
    return line;
  }

  public void addLayer(final ActionLine line) {
    sectionLines.add(line);
  }

  public int getHighestPriority() {
    return sectionLines.peek().getPriority();
  }

  public ActionLine getMostSignificant() {
    return sectionLines.peek();
  }

}
