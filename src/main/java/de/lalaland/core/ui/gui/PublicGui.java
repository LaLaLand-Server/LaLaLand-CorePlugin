package de.lalaland.core.ui.gui;

import de.lalaland.core.ui.gui.impl.AbstractGui;
import de.lalaland.core.ui.gui.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public abstract class PublicGui extends AbstractGui {


  public PublicGui(GuiManager guiManager, String title, InventoryType type, int raws, boolean filled) {
    super(guiManager, title, type, raws, filled);
  }

  public PublicGui(GuiManager guiManager, String title, InventoryType type, boolean filled) {
    super(guiManager, title, type, filled);
  }

  public PublicGui(GuiManager guiManager, String title, int raws, boolean filled) {
    super(guiManager, title, raws, filled);
  }

  public void preRender() {
    update();
  }

  @Override
  public void open(Player player) {

  }

  @Override
  public void close(Player player) {

  }
}