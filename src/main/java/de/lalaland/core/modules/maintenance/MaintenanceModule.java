package de.lalaland.core.modules.maintenance;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;

public class MaintenanceModule implements IModule {

  private MaintenanceManager maintenanceManager;

  @Override
  public String getModuleName() {
    return "MaintenanceModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {
    maintenanceManager = new MaintenanceManager(plugin);
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }


}
