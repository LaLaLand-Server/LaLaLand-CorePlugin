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
import de.lalaland.core.utils.common.UtilChunk;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

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

  @Override
  public String getModuleName() {
    return "ProtectionModule";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    this.regionManager = new RegionManager(this);
    gson = new GsonBuilder()
        .registerTypeAdapter(RuleSet.class, new RuleSetSerializer())
        .registerTypeAdapter(RuleSet.class, new RuleSetDeserializer())
        .registerTypeAdapter(ProtectedRegion.class, new ProtectedRegionSerializer())
        .registerTypeAdapter(ProtectedRegion.class, new ProtectedRegionDeserializer(regionManager))
        .setPrettyPrinting()
        .create();
    Bukkit.getPluginManager().registerEvents(new RegionListener(this.regionManager), plugin);
    plugin.getCommandManager().registerCommand(new RegionCommand(this.regionManager));
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
