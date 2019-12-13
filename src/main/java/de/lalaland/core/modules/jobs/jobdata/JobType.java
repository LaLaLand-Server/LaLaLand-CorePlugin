package de.lalaland.core.modules.jobs.jobdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum JobType {

  MINER("Bergbau"),
  WOODCUTTER("Holzfällerei"),
  HERBLORE("Botanik"), // Pflanzenkunde?
  FISHING("Fischen"),

  CONSTRUCTION("Konstruktion"),
  SMITHING("Schmieden"),
  CRAFTING("Handwerk"),
  COOKING("Kochen"),
  ;

  @Getter
  private final String displayName;

}