package de.lalaland.core.modules.loot.tables;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap.Entry;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import java.util.concurrent.ThreadLocalRandom;
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
public class LootTable implements LootTableEntry {

  public static LootTableBuilder builder() {
    return new LootTableBuilder();
  }

  public LootTable() {
    random = ThreadLocalRandom.current();
    entryMap = new Double2ObjectOpenHashMap<>();
  }

  private final ThreadLocalRandom random;
  private final Double2ObjectMap<LootTableEntry> entryMap;

  public void addEntry(final double chance, final LootTableEntry entry) {
    Preconditions.checkArgument(chance <= 1.0 && chance >= 0);
    entryMap.put(chance, entry);
  }

  @Override
  public void drop(final Location location) {
    for (final Entry<LootTableEntry> entry : entryMap.double2ObjectEntrySet()) {
      if (entry.getDoubleKey() > random.nextDouble()) {
        entry.getValue().drop(location);
      }
    }
  }

  @Override
  public void drop(final Inventory inventory, final Location location) {
    for (final Entry<LootTableEntry> entry : entryMap.double2ObjectEntrySet()) {
      if (entry.getDoubleKey() > random.nextDouble()) {
        entry.getValue().drop(inventory, location);
      }
    }
  }

}