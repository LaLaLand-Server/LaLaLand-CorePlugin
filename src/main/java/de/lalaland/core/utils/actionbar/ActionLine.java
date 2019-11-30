package de.lalaland.core.utils.actionbar;

import com.google.common.base.Strings;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionLine implements Comparable<ActionLine> {

  public static int MIN_PRIORITY = 1000000;
  public static int LOW_PRIORITY = 100000;
  public static int MID_PRIORITY = 10000;
  public static int HIGH_PRIORITY = 1000;
  public static int VERY_HIGH_PRIORITY = 100;
  public static int MAX_PRIORITY = 10;

  public static ActionLine empty() {
    return new ActionLine(MIN_PRIORITY, () -> Strings.repeat(" ", ActionBarBoard.MIN_SECTION_LENGTH));
  }

  public ActionLine(final int priority, final Supplier<String> lineSupplier) {
    this.priority = priority;
    this.lineSupplier = lineSupplier;
  }

  @Getter
  @Setter
  private final int priority;
  @Getter
  @Setter
  private final Supplier<String> lineSupplier;

  @Override
  public int compareTo(@NotNull final ActionLine other) {
    return priority - other.priority;
  }
}
