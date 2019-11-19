package de.lalaland.core.utils.anvilgui;

import com.google.common.collect.Maps;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.utils.anvilgui.AnvilGUI.Slot;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AnvilListener implements Listener {

  private static Map<Player, AnvilGUI> openInventories = Maps.newHashMap();

  public AnvilListener(CorePlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  public void add(Player player, AnvilGUI gui) {
    openInventories.put(player, gui);
  }

  public void remove(Player player) {
    openInventories.remove(player);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    final Player clicker = (Player) e.getWhoClicked();
    if (!openInventories.containsKey(clicker)) {
      return;
    }

    Inventory inventory = openInventories.get(clicker).getInventory();

    if (!e.getInventory().equals(inventory)) {
      return;
    }

    e.setCancelled(true);
    if (e.getRawSlot() != Slot.OUTPUT) {
      return;
    }

    final ItemStack clicked = inventory.getItem(e.getRawSlot());
    final AnvilGUI gui = openInventories.get(clicker);
    if (clicked == null || clicked.getType() == Material.AIR) {
      return;
    }

    final String ret = gui.getBiFunction().apply(clicker,
        clicked.hasItemMeta() ? clicked.getItemMeta().getDisplayName()
            : clicked.getType().toString());

    if (ret != null) {
      final ItemMeta meta = clicked.getItemMeta();
      meta.setDisplayName(ret);
      clicked.setItemMeta(meta);
      inventory.setItem(e.getRawSlot(), clicked);
    } else {
      gui.closeInventory();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e) {
    if (!openInventories.containsKey((Player) e.getPlayer())) {
      return;
    }

    AnvilGUI gui = openInventories.get((Player) e.getPlayer());

    if (gui.isOpen() && e.getInventory().equals(gui.getInventory())) {
      gui.closeInventory();
      openInventories.remove((Player) e.getPlayer());
    }
  }

}
