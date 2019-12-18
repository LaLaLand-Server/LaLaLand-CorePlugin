package de.lalaland.core.modules.loot.tables;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import lombok.Getter;
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
public class LootTableManager {

  @Getter
  private static LootTableManager instance;

  public LootTableManager() {
    lootTableCollectionMap = new Object2ObjectOpenHashMap<>();
    instance = this;
  }

  private final Map<String, LootTableEntry> lootTableCollectionMap;

  public void addTableCollection(final String tableID, final LootTableEntry lootTableEntry) {
    lootTableCollectionMap.put(tableID, lootTableEntry);
  }

  public void dropTable(final String tableID, final Location location) {
    final LootTableEntry lootTableEntry = lootTableCollectionMap.get(tableID);
    if (lootTableEntry != null) {
      lootTableEntry.drop(location);
    }
  }

  public void dropTable(final String tableID, final Inventory inventory, final Location location) {
    final LootTableEntry lootTableEntry = lootTableCollectionMap.get(tableID);
    if (lootTableEntry != null) {
      lootTableEntry.drop(inventory, location);
    }
  }

  public LootTableEntry getTableEntry(final String tableID) {
    return lootTableCollectionMap.get(tableID);
  }

}