package de.lalaland.core.modules.mobs.modeledentities;

import java.util.UUID;
import org.bukkit.entity.ArmorStand;
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
public interface ComplexModel<T> {

  public abstract UUID getEntityID();

  public abstract T getModel();

  public abstract LivingEntity getBukkit();

  public abstract boolean isMoving();

  public abstract ArmorStand getToken();

  public abstract void equipNormal();

  public abstract void equipMoving();

  public abstract void equipHurt();

  public abstract void equipAttack();

  public abstract int getAttackFrames();

}
