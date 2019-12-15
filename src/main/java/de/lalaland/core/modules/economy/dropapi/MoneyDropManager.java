package de.lalaland.core.modules.economy.dropapi;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.resourcepack.skins.Model;
import de.lalaland.core.utils.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MoneyDropManager {

  public MoneyDropManager(final CorePlugin plugin) {
    moneyDropModels = TreeRangeMap.create();
    moneyKey = "_MONEY";
    Bukkit.getPluginManager().registerEvents(new MoneyDropListener(plugin, this), plugin);
    moneyDropModels.put(Range.atMost(10), Model.COINPILE_TINY);
    moneyDropModels.put(Range.openClosed(10, 50), Model.COINPILE_SMALL);
    moneyDropModels.put(Range.openClosed(50, 500), Model.COINPILE_MEDIUM);
    moneyDropModels.put(Range.openClosed(500, 2500), Model.COINPILE_BIG);
    moneyDropModels.put(Range.openClosed(2500, 20000), Model.COINPILE_HUGE);
    moneyDropModels.put(Range.openClosed(20000, 250000), Model.COINPILE_BAR_SMALL);
    moneyDropModels.put(Range.openClosed(250000, 1000000), Model.COINPILE_BAR_MEDIUM);
    moneyDropModels.put(Range.atLeast(1000000), Model.COINPILE_BAR_BIG);
  }

  private final TreeRangeMap<Integer, Model> moneyDropModels;
  private final String moneyKey;

  protected ItemStack createItemStack(final int amount) {
    final NBTItem nbt = new NBTItem(moneyDropModels.get(amount).getItem());
    nbt.setInt(moneyKey, amount);
    return nbt.getItem();
  }

  public void dropMoney(final Location location, int amount) {
    int sum = 0;
    for (final Item nearItem : location.getNearbyEntitiesByType(Item.class, 2)) {
      final int val = getMoneyValue(nearItem);
      if (val > 0) {
        sum += val;
        nearItem.remove();
      }
    }
    if (sum > 0) {
      amount += sum;
    }
    final Item item = location.getWorld().dropItem(location, createItemStack(amount));
    item.getScoreboardTags().add(moneyKey);
    item.setCustomNameVisible(true);
    item.setCustomName("§e" + amount + " Gold");
  }

  public int getMoneyValue(final Item item) {
    int value = 0;
    if (item.getScoreboardTags().contains(moneyKey)) {
      value = new NBTItem(item.getItemStack()).getInt(moneyKey);
    }
    return value;
  }

}
