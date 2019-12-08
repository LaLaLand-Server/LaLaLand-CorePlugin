package de.lalaland.core.modules.mobs.modeledentities;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.IBiPiped;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MobModelManager {

  public MobModelManager(final CorePlugin plugin) {
    bipipedMappings = new Object2ObjectOpenHashMap<>();
    Bukkit.getPluginManager().registerEvents(new ModelListener(plugin, this), plugin);
  }

  private final Object2ObjectMap<UUID, IBiPiped> bipipedMappings;

  public void addBiModel(final IBiPiped piped) {
    bipipedMappings.put(piped.getEntityID(), piped);
  }

  public IBiPiped getBiModel(final UUID entityID) {
    return bipipedMappings.get(entityID);
  }

  public void removeBiModel(final UUID entityID) {
    bipipedMappings.remove(entityID);
  }

}