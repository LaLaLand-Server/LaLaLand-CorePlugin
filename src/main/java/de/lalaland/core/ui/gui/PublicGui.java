package de.lalaland.core.ui.gui;

import de.lalaland.core.ui.gui.impl.AbstractGui;
import org.bukkit.event.inventory.InventoryType;

public abstract class PublicGui extends AbstractGui {


    public PublicGui(String title, InventoryType type, int raws, boolean filled) {
        super(title, type, raws, filled);
    }

    public PublicGui(String title, InventoryType type, boolean filled) {
        super(title, type, filled);
    }

    public PublicGui(String title, int raws, boolean filled) {
        super(title, raws, filled);
    }

    public void preRender(){
        update();
    }

}