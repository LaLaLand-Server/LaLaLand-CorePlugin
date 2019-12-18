package de.lalaland.core.modules.loot.tables;

import de.lalaland.core.modules.loot.tables.impl.ItemDrop;
import de.lalaland.core.modules.loot.tables.impl.MoneyDrop;
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
public class LootTableBuilder {

  public LootTableBuilder() {
    lootTable = new LootTable();
  }

  private final LootTable lootTable;

  public LootTableBuilder addItemDrops(final double chance, final ItemStack... items) {
    lootTable.addEntry(chance, new ItemDrop(items));
    return this;
  }

  public LootTableBuilder addMoneyDrop(final double chance, final int lower, final int upper) {
    lootTable.addEntry(chance, new MoneyDrop(lower, upper));
    return this;
  }

  public LootTableBuilder addTable(final double chance, final LootTable table) {
    lootTable.addEntry(chance, table);
    return this;
  }

  public LootTable get() {
    return lootTable;
  }

  public LootTableCollection getAsSingleCollection() {
    return LootTableCollection.ofSingle(lootTable);
  }

}