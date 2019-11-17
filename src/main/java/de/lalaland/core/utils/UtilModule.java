package de.lalaland.core.utils;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.utils.items.display.ItemDisplayCompiler;

public class UtilModule implements IModule {

  @Override
  public String getModuleName() {
    return "Utility Module";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    plugin.setDisplayCompiler(new ItemDisplayCompiler(plugin));
    plugin.getProtocolManager().addPacketListener(plugin.getDisplayCompiler());
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
