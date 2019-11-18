package de.lalaland.core.ui.gui;

import de.lalaland.core.ui.gui.impl.AbstractGui;
import org.bukkit.event.inventory.InventoryType;

import java.util.UUID;

public abstract class PrivateGui extends AbstractGui {

    private final UUID uuid;

    public PrivateGui(String title, InventoryType type, int raws, boolean filled) {
        super(title, type, raws, filled);
        this.uuid = UUID.randomUUID();
    }

    public PrivateGui(String title, InventoryType type, boolean filled) {
        super(title, type, filled);
        this.uuid = UUID.randomUUID();
    }

    public PrivateGui(String title, int raws, boolean filled) {
        super(title, raws, filled);
        this.uuid = UUID.randomUUID();
    }


    public UUID getUuid() {
        return uuid;
    }
}