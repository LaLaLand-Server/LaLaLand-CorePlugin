package de.lalaland.core.ui.gui.listener;

import de.lalaland.core.ui.gui.impl.IGui;
import de.lalaland.core.ui.gui.manager.GuiManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 18.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class InventoryClickGuiListener implements Listener {

  public InventoryClickGuiListener(final GuiManager guiManager) {
    this.guiManager = guiManager;
  }

  private final GuiManager guiManager;

  @EventHandler
  public void onInventoryClickGui(final InventoryClickEvent event) {
    final Player player = (Player) event.getWhoClicked();

    final Inventory clickedInventory = event.getClickedInventory();
    final ItemStack currentItem = event.getCurrentItem();

    if ((clickedInventory == null) || (currentItem == null) || (currentItem.getType()
        .equals(Material.AIR))) {
      return;
    }
    final IGui iGui = guiManager.getGuiFromInventory(clickedInventory);
    if (iGui == null) {
      return;
    }
    event.setCancelled(true);
    iGui.handleClickEvent(event);
  }
}
