package de.lalaland.core.modules.schematics.core;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.schematics.workload.PasteThread;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicManager {

  public SchematicManager(final CorePlugin plugin) {
    schematicCashe = new Object2ObjectOpenHashMap<>();
    pasteThread = new PasteThread();
    plugin.getTaskManager().runRepeatedBukkit(pasteThread, 1L, 1L);
  }

  @Getter
  private final PasteThread pasteThread;
  private final Object2ObjectOpenHashMap<String, AbstractSchematic> schematicCashe;

  @Nullable
  public AbstractSchematic getSchematic(final String schematicID) {
    return schematicCashe.get(schematicID);
  }

  public SimpleSchematic createSimple(final BoundingBox region, final String schematicID) {
    final SimpleSchematic schematic = new SimpleSchematic(region, schematicID);
    schematicCashe.put(schematicID, schematic);
    return schematic;
  }

  public SimpleSchematic createSimple(final ProtectedRegion region, final String schematicID) {
    return createSimple(region.getBoundingBoxClone(), schematicID);
  }

}
