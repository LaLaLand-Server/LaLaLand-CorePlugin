package de.lalaland.core.utils.guis;

import org.bukkit.event.inventory.InventoryType;

public class InventoryGUIBuilder {

  protected InventoryGUIBuilder() {
    this(InventoryType.CHEST);
  }

  protected InventoryGUIBuilder(final InventoryType inventoryType) {
    this.inventoryType = inventoryType;
  }

  private final InventoryType inventoryType;

}