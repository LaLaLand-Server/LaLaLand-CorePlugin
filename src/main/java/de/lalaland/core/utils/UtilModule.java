package de.lalaland.core.utils;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.utils.guis.GUIManager;
import de.lalaland.core.utils.guis.InventoryGUI;

public class UtilModule implements IModule {

  @Override
  public String getModuleName() {
    return "Utility Module";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    GUIManager.init(plugin);
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
