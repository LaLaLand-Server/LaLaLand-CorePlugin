package de.lalaland.core.modules.mobs.custommobs;

import de.lalaland.core.modules.mobs.modeledentities.ComplexModel;
import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
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
public class CustomMobManager {

  public CustomMobManager(final MobModelManager mobModelManager) {
    this.mobModelManager = mobModelManager;
  }

  private final MobModelManager mobModelManager;

  public ComplexModel<?> spawnModeled(final ComplexModelType type, final Location location) {
    return type.getModelFactory().spawn(location, mobModelManager);
  }

  public LivingEntity spawnPlayerDisguised(final EntityType type, final String playerName, final Location location) {
    final PlayerDisguise disguise = new PlayerDisguise(playerName);
    final LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type);
    disguise.setEntity(entity);
    disguise.startDisguise();
    return entity;
  }


}
