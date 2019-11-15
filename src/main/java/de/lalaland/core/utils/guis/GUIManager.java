package de.lalaland.core.utils.guis;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIManager implements Listener {

  @Getter(AccessLevel.PROTECTED)
  private static GUIManager instance = null;

  public static void init(final JavaPlugin plugin) {
    instance = new GUIManager(plugin);
    Bukkit.getPluginManager().registerEvents(instance, plugin);
  }

  private GUIManager(final JavaPlugin plugin) {
    guiCache = new Object2ObjectOpenHashMap<>();
  }

  private final Object2ObjectOpenHashMap<Inventory, InventoryGUI> guiCache;

  protected void addInventoryGUI(final Inventory inventory, final InventoryGUI inventoryGUI) {
    guiCache.put(inventory, inventoryGUI);
  }

  @EventHandler
  public void handleInventoryClick(final InventoryClickEvent event) {
    final Inventory inv = event.getInventory();
    final InventoryGUI gui = guiCache.get(inv);
    if (gui != null) {
      gui.handleClickEvent(event);
    }
  }

  @EventHandler
  public void handleInventoryClose(final InventoryCloseEvent event) {
    final Inventory inv = event.getInventory();
    final InventoryGUI gui = guiCache.get(inv);
    if (gui != null) {
      gui.handleClose(event);
    }
  }

}