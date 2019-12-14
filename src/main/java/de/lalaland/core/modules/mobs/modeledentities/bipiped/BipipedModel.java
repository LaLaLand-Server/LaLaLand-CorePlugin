package de.lalaland.core.modules.mobs.modeledentities.bipiped;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum BipipedModel {

  TEST(BiModelEquipment.ofModelIDs(1029, 1028, 1027, 1030, 1030, 1029, 1028, 1027, 1030, 1030));

  @Getter
  private final BiModelEquipment equipment;

}