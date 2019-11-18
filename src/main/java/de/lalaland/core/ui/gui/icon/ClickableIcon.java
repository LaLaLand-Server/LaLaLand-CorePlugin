package de.lalaland.core.ui.gui.icon;

import de.lalaland.core.ui.gui.icon.impl.Clickable;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableIcon extends SimpleIcon implements Clickable {

    public ClickableIcon(ItemStack displayItem){
        super(displayItem);
    }

}