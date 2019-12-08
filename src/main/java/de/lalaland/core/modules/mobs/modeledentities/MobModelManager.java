package de.lalaland.core.modules.mobs.modeledentities;

import de.lalaland.core.CorePlugin;
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

  private final Object2ObjectMap<UUID, ComplexModel<?>> bipipedMappings;

  public void addModel(final ComplexModel<?> model) {
    bipipedMappings.put(model.getEntityID(), model);
  }

  public ComplexModel<?> getModel(final UUID entityID) {
    return bipipedMappings.get(entityID);
  }

  public void removeModel(final UUID entityID) {
    bipipedMappings.remove(entityID);
  }

}