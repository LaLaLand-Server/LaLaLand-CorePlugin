package de.lalaland.core.modules.mobs.custommobs.zombie;

import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.BipipedModel;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TestBiZombie extends BiPipedZombie {

  public TestBiZombie(final World world,
      final MobModelManager mobModelManager,
      final EntityArmorStand token) {
    super(world, BipipedModel.TEST, mobModelManager, token);
  }

}
