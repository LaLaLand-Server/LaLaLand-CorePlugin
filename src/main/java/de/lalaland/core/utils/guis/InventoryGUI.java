package de.lalaland.core.utils.guis;

import de.lalaland.core.utils.tuples.Unit;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryGUI {

  public static InventoryGUIBuilder builder() {
    return new InventoryGUIBuilder();
  }

  protected InventoryGUI(final Inventory inventory) {
    clickHandler = new Int2ObjectOpenHashMap<>();
    closeHandler = Unit.of(null);
    localClickHandler = Unit.of(null);
    this.inventory = inventory;
    GUIManager.getInstance().addInventoryGUI(inventory, this);
  }

  private final Inventory inventory;
  private final Int2ObjectOpenHashMap<Consumer<InventoryClickEvent>> clickHandler;
  private final Unit<Consumer<InventoryCloseEvent>> closeHandler;
  private final Unit<Consumer<InventoryClickEvent>> localClickHandler;

  protected void handleClickEvent(final InventoryClickEvent event) {
    event.setCancelled(true);
    if (localClickHandler.isPresent()) {
      localClickHandler.getValue().accept(event);
    }
    final Consumer<InventoryClickEvent> eventConsumer = clickHandler.get(event.getSlot());
    if (eventConsumer != null) {
      eventConsumer.accept(event);
    }
  }

  protected void setClickHandler(final int slot,
      final Consumer<InventoryClickEvent> eventConsumer) {
    clickHandler.put(slot, eventConsumer);
  }

  protected void handleClose(final InventoryCloseEvent event) {
    if (closeHandler.isPresent()) {
      closeHandler.getValue().accept(event);
    }
  }

  protected void setCloseHandler(final Consumer<InventoryCloseEvent> eventConsumer) {
    closeHandler.setValue(eventConsumer);
  }

  protected void setLocalClickHandler(final Consumer<InventoryClickEvent> eventConsumer) {
    localClickHandler.setValue(eventConsumer);
  }

}
