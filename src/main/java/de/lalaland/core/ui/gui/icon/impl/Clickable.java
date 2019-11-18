package de.lalaland.core.ui.gui.icon.impl;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {

    void handleRightClick(InventoryClickEvent event);

    void handleLeftClick(InventoryClickEvent event);

}
