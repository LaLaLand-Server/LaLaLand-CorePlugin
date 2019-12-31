package de.lalaland.core.modules.loot.tables.impl;

import de.lalaland.core.modules.loot.protection.DropProtection;
import de.lalaland.core.modules.loot.tables.LootTableEntry;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 17.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemDrop implements LootTableEntry {

  public ItemDrop(final ItemStack... items) {
    this.items = items;
  }

  private final ItemStack[] items;

  @Override
  public void drop(final Location location) {
    final World world = location.getWorld();
    for (final ItemStack item : items) {
      location.getWorld().dropItem(location, item);
    }
  }

  @Override
  public void drop(final Inventory inventory, final Location location) {
    for (final ItemStack itemStack : items) {
      inventory.addItem(itemStack).values().forEach(remaining -> {
        location.getWorld().dropItemNaturally(location, remaining);
      });
    }
  }

  @Override
  public void dropProtected(Location location, UUID... uuids) {
    final World world = location.getWorld();
    for (final ItemStack item : items) {
      DropProtection.protect(location.getWorld().dropItem(location, item), uuids);
    }
  }

  @Override
  public void dropProtected(Inventory inventory, Location location, UUID... uuids) {
    for (final ItemStack itemStack : items) {
      inventory.addItem(itemStack).values().forEach(remaining -> {
        DropProtection.protect(location.getWorld().dropItemNaturally(location, remaining), uuids);
      });
    }
  }

}