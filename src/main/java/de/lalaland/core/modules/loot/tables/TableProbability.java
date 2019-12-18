package de.lalaland.core.modules.loot.tables;

import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 17.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum TableProbability {

  ALWAYS(1.0D), // 1 in 1
  COMMON(0.25D), // 1 in 4
  UNCOMMON(0.0625D), // 1 in 16
  RARE(0.015625D), // 1 in 64
  VERY_RARE(0.00390625D), // 1 in 512
  ULTRA_RARE(0.0009765625D); // 1 in 1024

  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

  private final double chance;

  public boolean roll() {
    return chance > RANDOM.nextDouble();
  }

}