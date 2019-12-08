package de.lalaland.core.modules.mobs.custommobs.zombie;

import de.lalaland.core.modules.mobs.custommobs.CustomMob;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityZombie;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.entity.LivingEntity;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CustomZombieMob extends EntityZombie implements CustomMob {

  public CustomZombieMob(final World world) {
    super(EntityTypes.ZOMBIE, world);
  }

  @Override
  public EntityLiving getAsNMSEntity() {
    return this;
  }

  @Override
  public LivingEntity getAsBukkitEntity() {
    return getBukkitLivingEntity();
  }

}
