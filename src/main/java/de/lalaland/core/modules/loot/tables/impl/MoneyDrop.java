package de.lalaland.core.modules.loot.tables.impl;

import com.google.common.base.Preconditions;
import de.lalaland.core.modules.economy.dropapi.MoneyDropManager;
import de.lalaland.core.modules.loot.protection.DropProtection;
import de.lalaland.core.modules.loot.tables.LootTableEntry;
import java.util.UUID;
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
public class MoneyDrop implements LootTableEntry {

  public MoneyDrop(final int lowerLimit, int upperLimit) {
    Preconditions.checkArgument(lowerLimit <= upperLimit);
    if (upperLimit == lowerLimit) {
      upperLimit++;
    }
    random = ThreadLocalRandom.current();
    lower = lowerLimit;
    upper = upperLimit;
    moneyDropManager = MoneyDropManager.getInstance();
  }

  private final ThreadLocalRandom random;
  private final int lower;
  private final int upper;
  private final MoneyDropManager moneyDropManager;

  private int roll() {
    return random.nextInt(lower, upper);
  }

  @Override
  public void drop(final Location location) {
    moneyDropManager.dropMoney(location, roll());
  }

  @Override
  public void drop(final Inventory inventory, final Location location) {
    drop(location);
  }

  @Override
  public void dropProtected(Location location, UUID... uuids) {
    DropProtection.protect(moneyDropManager.dropMoney(location, roll()), uuids);
  }

  @Override
  public void dropProtected(Inventory inventory, Location location, UUID... uuids) {
    DropProtection.protect(moneyDropManager.dropMoney(location, roll()), uuids);
  }

}
