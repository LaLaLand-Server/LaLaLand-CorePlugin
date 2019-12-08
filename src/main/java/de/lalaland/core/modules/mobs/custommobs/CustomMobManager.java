package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.IBiPiped;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomMobManager {

  public CustomMobManager(final MobModelManager mobModelManager) {
    this.mobModelManager = mobModelManager;
  }

  private final MobModelManager mobModelManager;

  public IBiPiped spawnBiPiped(final BiPipedType type, final Location location) {
    return type.getModelFactory().spawn(location, mobModelManager);
  }


}
