package de.lalaland.core.modules.protection;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.protection.regions.RegionListener;
import de.lalaland.core.modules.protection.regions.RegionManager;
import lombok.Getter;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 18.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectionModule implements IModule {

  @Getter
  private RegionManager regionManager;

  @Override
  public String getModuleName() {
    return "ProtectionModule";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    this.regionManager = new RegionManager();
    Bukkit.getPluginManager().registerEvents(new RegionListener(this.regionManager), plugin);
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
