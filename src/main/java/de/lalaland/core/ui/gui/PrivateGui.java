package de.lalaland.core.ui.gui;

import de.lalaland.core.ui.gui.impl.AbstractGui;
import de.lalaland.core.ui.gui.manager.GuiManager;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public abstract class PrivateGui extends AbstractGui {

  private final UUID uuid;

  public PrivateGui(GuiManager guiManager, String title, InventoryType type, int raws, boolean filled) {
    super(guiManager, title, type, raws, filled);
    this.uuid = UUID.randomUUID();
  }

  public PrivateGui(GuiManager guiManager, String title, InventoryType type, boolean filled) {
    super(guiManager, title, type, filled);
    this.uuid = UUID.randomUUID();
  }

  public PrivateGui(GuiManager guiManager, String title, int raws, boolean filled) {
    super(guiManager, title, raws, filled);
    this.uuid = UUID.randomUUID();
  }


  public UUID getUuid() {
    return uuid;
  }

  @Override
  public void open(Player player) {

  }

  @Override
  public void close(Player player) {

  }
}