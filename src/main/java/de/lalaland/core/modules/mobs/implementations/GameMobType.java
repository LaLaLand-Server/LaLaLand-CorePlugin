package de.lalaland.core.modules.mobs.implementations;

import de.lalaland.core.modules.mobs.implementations.dev.TestBiPiped;
import de.lalaland.core.modules.mobs.implementations.dev.TestDisguised;
import de.lalaland.core.modules.mobs.implementations.dev.TestSingleModel;
import java.util.function.Function;
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
public enum GameMobType {

  TEST_BI_MOB((lvl) -> new TestBiPiped(lvl)),
  TEST_DISGUISED((lvl) -> new TestDisguised(lvl)),
  TEST_SINGLE((lvl) -> new TestSingleModel(lvl));

  @Getter
  private final Function<Integer, GameMob> gameMobFunction;

}
