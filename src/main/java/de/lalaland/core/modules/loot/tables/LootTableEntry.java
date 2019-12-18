package de.lalaland.core.modules.loot.tables;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 17.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface LootTableEntry {

  public void drop(Location location);
  public void drop(Inventory inventory, Location location);

}
