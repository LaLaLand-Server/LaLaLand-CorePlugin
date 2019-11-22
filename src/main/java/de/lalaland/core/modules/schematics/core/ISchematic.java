package de.lalaland.core.modules.schematics.core;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract interface ISchematic {

  public abstract void paste(Location location);
  public abstract Vector getDimension();

}
