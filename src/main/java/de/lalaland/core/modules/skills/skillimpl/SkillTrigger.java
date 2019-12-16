package de.lalaland.core.modules.skills.skillimpl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum SkillTrigger {

  ACTIVE_CAST("Wirken"),
  CROUCH("Schleichen"),
  DEATH("Sterben"),
  KILL("Kill erzielen"),
  FALL("Fallschaden");

  @Getter
  private final String display;

}