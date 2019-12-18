package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.loot.tables.LootTable;
import de.lalaland.core.modules.loot.tables.LootTableEntry;
import java.util.EnumMap;
import java.util.function.Supplier;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 17.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourceLoot {

  public static void init() {

  }

  private static final LootTableEntry EMPTY_ENTRY = LootTable.builder().get();
  private static final EnumMap<ResourceType, Supplier<LootTableEntry>> RESOURCE_LOOT_MAP = new EnumMap<>(ResourceType.class);

  public static Supplier<LootTableEntry> of(final ResourceType type) {
    return RESOURCE_LOOT_MAP.getOrDefault(type, () -> EMPTY_ENTRY);
  }

}
