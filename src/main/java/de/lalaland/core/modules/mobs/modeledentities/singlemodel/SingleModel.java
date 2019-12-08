package de.lalaland.core.modules.mobs.modeledentities.singlemodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum SingleModel {

  TEST(SingleModelEquipment.ofModelIDs(10000, 10001, 10002, 10003, 10004));

  @Getter
  private final SingleModelEquipment singleModelEquipment;

}
