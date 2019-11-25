package de.lalaland.core.modules.structures;

import com.google.common.collect.Lists;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.protection.ProtectionModule;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.schematics.SchematicModule;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.modules.structures.core.StructureCommand;
import de.lalaland.core.modules.structures.core.StructureListener;
import de.lalaland.core.modules.structures.core.StructureManager;
import java.util.ArrayList;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class StructureModule implements IModule {

  private StructureManager structureManager;

  @Override
  public String getModuleName() {
    return "StructureModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    final RegionManager regionManager = plugin.getModule(ProtectionModule.class).getRegionManager();
    structureManager = new StructureManager(regionManager);
    final SchematicManager schematicManager = plugin.getModule(SchematicModule.class).getSchematicManager();
    Bukkit.getPluginManager().registerEvents(new StructureListener(regionManager, structureManager), plugin);
    plugin.getCommandManager().registerCommand(new StructureCommand(schematicManager, structureManager));
    plugin.getCommandManager().getCommandCompletions().registerStaticCompletion("Schematics", () ->{
      final ArrayList<String> schematicNames = Lists.newArrayList();
      schematicManager.forEach(s -> schematicNames.add(s.getSchmaticID()));
      return schematicNames;
    });
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
