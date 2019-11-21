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

  public static ActionLine empty() {
    return new ActionLine(0, () -> Strings.repeat(" ", ActionBarBoard.MIN_SECTION_LENGTH));
  }

  protected ActionLine(final int priority, final Supplier<String> lineSupplier) {
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
    return other.priority - priority;
  }
}
