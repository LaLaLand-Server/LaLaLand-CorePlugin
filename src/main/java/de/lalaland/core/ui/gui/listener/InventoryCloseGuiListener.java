package de.lalaland.core.ui.gui.listener;

import de.lalaland.core.ui.gui.impl.IGui;
import de.lalaland.core.ui.gui.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 18.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class InventoryCloseGuiListener implements Listener {

  private final GuiManager guiManager;

  public InventoryCloseGuiListener(final GuiManager guiManager) {
    this.guiManager = guiManager;
  }

  @EventHandler
  public void onInventoryGuiClose(final InventoryCloseEvent event) {

    final Inventory inventory = event.getInventory();
    final Player player = (Player) event.getPlayer();

    final IGui gui = guiManager.getGuiFromInventory(inventory);

    if (gui == null) {
      return;
    }

    gui.close(player);
  }
}
