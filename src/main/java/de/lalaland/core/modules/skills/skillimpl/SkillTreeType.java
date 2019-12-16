package de.lalaland.core.modules.skills.skillimpl;

import de.lalaland.core.modules.resourcepack.skins.Model;
import lombok.AllArgsConstructor;

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
public enum SkillTreeType {

  FIRE_MAGIC(Model.RED_X,"Feuermagie", new String[]{"Feuermagie halt"}),
  MARTIAL_ARTS(Model.RED_X,"Kampfkunst", new String[]{""}),
  PROTECTION(Model.RED_X,"Schutz", new String[]{""});

  private final Model model;
  private final String displayName;
  private final String[] description;

}
