package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.custommobs.zombie.BiTestFactory;
import de.lalaland.core.modules.mobs.custommobs.zombie.SingleTestFactory;
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
public enum ComplexModelType {

  TEST_BI(new BiTestFactory()),
  TEST_SINGLE(new SingleTestFactory());

  @Getter
  private final ComplexModelFactory<?> modelFactory;

}
