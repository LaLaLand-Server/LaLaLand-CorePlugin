package de.lalaland.core.modules.jobs.jobdata;

import de.lalaland.core.modules.resourcepack.skins.Model;
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

  MINING("Bergbau", Model.RED_X),
  WOODCUTTER("Holzfällerei", Model.RED_X),
  HERBLORE("Botanik", Model.RED_X), // Pflanzenkunde?
  FISHING("Fischen", Model.RED_X),

  CONSTRUCTION("Konstruktion", Model.RED_X),
  SMITHING("Schmieden", Model.RED_X),
  BREWING("Brauen", Model.RED_X),
  CRAFTING("Handwerk", Model.RED_X),
  JEWEL_CRAFTING("Schmuck Handwerk", Model.RED_X),
  COOKING("Kochen", Model.RED_X);

  @Getter
  private final String displayName;
  @Getter
  private final Model model;

}