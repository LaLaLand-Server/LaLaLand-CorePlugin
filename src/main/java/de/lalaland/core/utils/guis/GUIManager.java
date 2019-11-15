package de.lalaland.core.utils.guis;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIManager {

    public static void init(final JavaPlugin plugin){
      new GUIManager(plugin);
    }

    private GUIManager(final JavaPlugin plugin){
      guiCache = new Object2ObjectOpenHashMap<>();
    }

    private final Object2ObjectOpenHashMap<Inventory, InventoryGUI> guiCache;

}
