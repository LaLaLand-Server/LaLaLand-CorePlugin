package de.lalaland.core.modules.economy.dropapi;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.User;
import de.lalaland.core.user.UserManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MoneyDropListener implements Listener {

  protected MoneyDropListener(final CorePlugin plugin, final MoneyDropManager moneyDropManager) {
    userManager = plugin.getUserManager();
    this.moneyDropManager = moneyDropManager;
  }

  private final UserManager userManager;
  private final MoneyDropManager moneyDropManager;

  @EventHandler
  public void onPickup(final EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    if (!event.getItem().getScoreboardTags().contains("_MONEY")) {
      return;
    }
    final int money = moneyDropManager.getMoneyValue(event.getItem());
    if (money < 0) {
      return;
    }
    event.getItem().remove();
    final User user = userManager.getUser(event.getEntity().getUniqueId());
    user.addMoney(money, false);
    event.setCancelled(true);
  }

  @EventHandler
  public void onMerge(final ItemMergeEvent event) {
    final Item from = event.getEntity();
    final Item to = event.getTarget();
    final int valFrom = moneyDropManager.getMoneyValue(from);
    final int valTo = moneyDropManager.getMoneyValue(to);
    if (valFrom < 1 || valTo < 1) {
      return;
    }
    final int sum = valFrom + valTo;
    event.setCancelled(true);
    from.remove();
    to.setItemStack(moneyDropManager.createItemStack(sum));
    to.setCustomName("§e" + sum + " Gold");
  }

}