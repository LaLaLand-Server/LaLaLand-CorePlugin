package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.IBiPiped;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface BiPipedFactory <T extends IBiPiped> {

  public T spawn(Location location, MobModelManager mobModelManager);

}
