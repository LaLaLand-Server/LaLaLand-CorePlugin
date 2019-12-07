package de.lalaland.core.modules.mobs.modeledentities;

import de.lalaland.core.modules.mobs.modeledentities.bipiped.BipipedModel;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.entity.LivingEntity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ModelManager {

  public ModelManager() {
    bipipedMappings = new Object2ObjectOpenHashMap<>();
  }

  private final Object2ObjectMap<LivingEntity, BipipedModel> bipipedMappings;

  public BipipedModel getModelOf(final LivingEntity entity) {
    return bipipedMappings.get(entity);
  }

  protected void remove(final LivingEntity entity) {
    bipipedMappings.remove(entity);
  }

}