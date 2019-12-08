package de.lalaland.core.modules.mobs.custommobs;

import net.minecraft.server.v1_14_R1.EntityLiving;
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
public interface CustomMob {

  public EntityLiving getAsNMSEntity();
  public LivingEntity getAsBukkitEntity();

}
