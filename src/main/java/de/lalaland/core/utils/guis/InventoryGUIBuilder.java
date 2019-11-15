package de.lalaland.core.utils.guis;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.Consumer;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryGUIBuilder {

  private static final String NO_TITLE = "Kein Titel";

  public InventoryGUIBuilder() {
    this(InventoryType.CHEST);
  }

  public InventoryGUIBuilder(final InventoryType inventoryType) {
    this.inventoryType = inventoryType;
    clickHandlers = new Int2ObjectOpenHashMap<>();
    closeHandler = null;
    localClickHandler = null;
  }

  private final InventoryType inventoryType;
  @Setter
  private int size = -1;
  @Setter
  private String title = NO_TITLE;
  private final Int2ObjectOpenHashMap<Consumer<InventoryClickEvent>> clickHandlers;
  private Consumer<InventoryCloseEvent> closeHandler;
  private Consumer<InventoryClickEvent> localClickHandler;

  public InventoryGUIBuilder setCloseHandler(final Consumer<InventoryCloseEvent> eventConsumer) {
    closeHandler = eventConsumer;
    return this;
  }

  public InventoryGUIBuilder addClickHandler(final int slot,
      final Consumer<InventoryClickEvent> eventConsumer) {
    clickHandlers.put(slot, eventConsumer);
    return this;
  }

  public InventoryGUIBuilder setLocalClickHandler(
      final Consumer<InventoryClickEvent> eventConsumer) {
    localClickHandler = eventConsumer;
    return this;
  }

  public InventoryGUI build() {
    final Inventory inv;
    if (size == -1) {
      inv = Bukkit.createInventory(null, inventoryType, title);
    } else {
      inv = Bukkit.createInventory(null, size, title);
    }
    final InventoryGUI gui = new InventoryGUI(inv);

    return gui;
  }

}