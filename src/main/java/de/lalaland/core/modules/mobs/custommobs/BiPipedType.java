package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.custommobs.zombie.BiTestFactory;
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
public enum BiPipedType {

  TEST(new BiTestFactory());

  @Getter
  private final BiPipedFactory<?> modelFactory;

}
