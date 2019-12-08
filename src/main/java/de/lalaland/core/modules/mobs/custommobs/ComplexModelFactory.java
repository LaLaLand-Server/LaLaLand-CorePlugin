package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.modeledentities.ComplexModel;
import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
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
public interface ComplexModelFactory<T extends ComplexModel<?>> {

  public T spawn(Location location, MobModelManager mobModelManager);

}
