package de.lalaland.core.modules.jobs.jobdata;

import lombok.AllArgsConstructor;

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
  WOODCUTTER("Holzfäller"),
  GATHERER("Sammler");

  private final String displayName;

}
