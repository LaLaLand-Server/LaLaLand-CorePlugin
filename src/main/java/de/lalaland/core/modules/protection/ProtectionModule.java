package de.lalaland.core.modules.protection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionListener;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.protection.regions.RuleSet;
import de.lalaland.core.modules.protection.regions.editor.RegionCommand;
import de.lalaland.core.modules.protection.regions.serialization.ProtectedRegionDeserializer;
import de.lalaland.core.modules.protection.regions.serialization.ProtectedRegionSerializer;
import de.lalaland.core.modules.protection.regions.serialization.RuleSetDeserializer;
import de.lalaland.core.modules.protection.regions.serialization.RuleSetSerializer;
import java.io.File;
import java.io.IOException;
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
  @Getter
  private Gson gson;

  private File regionFolder;

  @Override
  public String getModuleName() {
    return "ProtectionModule";
  }

  @Override
  public void enable(final CorePlugin plugin) {
    regionFolder = new File(plugin.getDataFolder() + File.separator + "regions");
    if (!regionFolder.exists()) {
      regionFolder.mkdirs();
    }
    regionManager = new RegionManager(this);
    gson = new GsonBuilder()
        .registerTypeAdapter(RuleSet.class, new RuleSetSerializer())
        .registerTypeAdapter(RuleSet.class, new RuleSetDeserializer())
        .registerTypeAdapter(ProtectedRegion.class, new ProtectedRegionSerializer())
        .registerTypeAdapter(ProtectedRegion.class, new ProtectedRegionDeserializer(
            regionManager))
        .setPrettyPrinting()
        .create();

    Bukkit.getPluginManager().registerEvents(new RegionListener(regionManager), plugin);
    plugin.getCommandManager().registerCommand(new RegionCommand(regionManager));

    try {
      regionManager.loadRegions(regionFolder);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disable(final CorePlugin plugin) {
    try {
      regionManager.saveRegions(regionFolder);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
