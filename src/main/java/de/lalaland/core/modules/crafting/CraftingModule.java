package de.lalaland.core.modules.crafting;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.crafting.listeners.CraftingInteractListener;
import org.bukkit.Bukkit;

public class CraftingModule implements IModule {

  @Override
  public String getModuleName() {
    return "CraftingModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {

    Bukkit.getPluginManager().registerEvents(new CraftingInteractListener(plugin),plugin);
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }


}
