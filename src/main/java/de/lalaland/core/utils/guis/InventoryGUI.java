package de.lalaland.core.utils.guis;

import de.lalaland.core.utils.tuples.Unit;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryGUI {

  public static InventoryGUIBuilder builder() {
    return new InventoryGUIBuilder();
  }

  protected InventoryGUI() {
    clickHandler = new Int2ObjectOpenHashMap<>();
    closeHandler = Unit.of(null);
  }

  private final Int2ObjectOpenHashMap<Consumer<InventoryClickEvent>> clickHandler;
  private final Unit<Consumer<InventoryCloseEvent>> closeHandler;

  protected void handleClickEvent(final InventoryClickEvent event) {
    final Consumer<InventoryClickEvent> eventConsumer = clickHandler.get(event.getSlot());
    if (eventConsumer != null) {
      eventConsumer.accept(event);
    }
  }

  protected void addClickHandler(final int slot,
      final Consumer<InventoryClickEvent> eventConsumer) {
    clickHandler.put(slot, eventConsumer);
  }

  protected void handleClose(final InventoryCloseEvent event) {
    if (closeHandler.isPresent()) {
      closeHandler.getValue().accept(event);
    }
  }

  protected void addCloseHandler(final Consumer<InventoryCloseEvent> eventConsumer) {
    closeHandler.setValue(eventConsumer);
  }

}
