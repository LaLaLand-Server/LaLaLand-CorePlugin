package de.lalaland.core.ui.gui.manager;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.ui.gui.impl.IGui;
import de.lalaland.core.ui.gui.listener.InventoryClickGuiListener;
import de.lalaland.core.ui.gui.listener.InventoryCloseGuiListener;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 18.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class GuiManager {

  @Getter
  private final Object2ObjectOpenHashMap<Inventory, IGui> guis;


  public GuiManager(final CorePlugin corePlugin) {
    guis = new Object2ObjectOpenHashMap<>();
    final PluginManager pluginManager = corePlugin.getServer().getPluginManager();
    pluginManager.registerEvents(new InventoryClickGuiListener(this), corePlugin);
    pluginManager.registerEvents(new InventoryCloseGuiListener(this), corePlugin);
  }

  public boolean isCached(final Inventory inventory) {
    return guis.containsKey(inventory);
  }

  public IGui getGuiFromInventory(final Inventory inventory) {
    return guis.get(inventory);
  }

  public void addGui(final IGui gui) {
    guis.put(gui.getBukkitInventory(), gui);
  }

  public void removeGui(final IGui gui) {

    if (!isCached(gui.getBukkitInventory())) {
      return;
    }

    guis.remove(gui.getBukkitInventory(), gui);
  }

}
