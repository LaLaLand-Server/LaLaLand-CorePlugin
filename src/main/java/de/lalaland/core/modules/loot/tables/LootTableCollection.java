package de.lalaland.core.modules.loot.tables;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.UUID;
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
public class LootTableCollection implements LootTableEntry {

  public static LootTableCollection ofSingle(final LootTable table) {
    final LootTableCollection collection = new LootTableCollection();
    collection.setTable(TableProbability.ALWAYS, table);
    return collection;
  }

  public LootTableCollection() {
    lootTables = Maps.newEnumMap(TableProbability.class);
  }

  private final EnumMap<TableProbability, LootTable> lootTables;

  public void setTable(final TableProbability probability, final LootTable table) {
    lootTables.put(probability, table);
  }

  @Override
  public void drop(final Location location) {
    for (final Entry<TableProbability, LootTable> entry : lootTables.entrySet()) {
      if (entry.getKey().roll()) {
        entry.getValue().drop(location);
      }
    }
  }

  @Override
  public void drop(final Inventory inventory, final Location location) {
    for (final Entry<TableProbability, LootTable> entry : lootTables.entrySet()) {
      if (entry.getKey().roll()) {
        entry.getValue().drop(inventory, location);
      }
    }
  }

  @Override
  public void dropProtected(Location location, UUID... uuids) {
    for (final Entry<TableProbability, LootTable> entry : lootTables.entrySet()) {
      if (entry.getKey().roll()) {
        entry.getValue().dropProtected(location, uuids);
      }
    }
  }

  @Override
  public void dropProtected(Inventory inventory, Location location, UUID... uuids) {
    for (final Entry<TableProbability, LootTable> entry : lootTables.entrySet()) {
      if (entry.getKey().roll()) {
        entry.getValue().dropProtected(inventory, location, uuids);
      }
    }
  }

}