package de.lalaland.core.modules.maintenance;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.config.Config;
import de.lalaland.core.config.ConfigFileHandler;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

public class MaintenanceManager {

  //TODO work with fast utils. not implemented yet.

  private final CorePlugin corePlugin;
  @Getter
  private final List<UUID> acceptedUuids;
  @Getter
  private final String joinPermission = "maintenance.join";
  @Getter
  private boolean maintenanceEnabled;

  public MaintenanceManager(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;
    final Config config = corePlugin.getCoreConfig();
    acceptedUuids = config.getMaintenanceUUIDs();
    maintenanceEnabled = config.isMaintenanceEnabled();
  }

  public void setMaintenanceEnabled(final boolean maintenanceEnabled) {
    this.maintenanceEnabled = maintenanceEnabled;
    final ConfigFileHandler configFileHandler = new ConfigFileHandler(corePlugin);
    configFileHandler.saveConfig();
  }

  public void addAcceptedUUID(final UUID uuid){

    if(isAccepted(uuid)){
      return;
    }

    acceptedUuids.add(uuid);
    final ConfigFileHandler configFileHandler = new ConfigFileHandler(corePlugin);
    configFileHandler.saveConfig();
  }

  public void removeAcceptedUUID(final UUID uuid){

    if(!isAccepted(uuid)){
      return;
    }

    acceptedUuids.remove(uuid);
    final ConfigFileHandler configFileHandler = new ConfigFileHandler(corePlugin);
    configFileHandler.saveConfig();
  }

  public boolean isAccepted(final UUID uuid){
    return acceptedUuids.contains(uuid);
  }

  public void preventJoin(final PlayerLoginEvent loginEvent){

  }

}
