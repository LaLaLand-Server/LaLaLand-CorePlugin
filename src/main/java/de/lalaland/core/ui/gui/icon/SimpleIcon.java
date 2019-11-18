package de.lalaland.core.ui.gui.icon;

import de.lalaland.core.ui.gui.icon.impl.IIcon;
import org.bukkit.inventory.ItemStack;

public class SimpleIcon implements IIcon {

    private final ItemStack displayItem;

    public SimpleIcon(ItemStack displayItem){
        this.displayItem = displayItem;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }
}