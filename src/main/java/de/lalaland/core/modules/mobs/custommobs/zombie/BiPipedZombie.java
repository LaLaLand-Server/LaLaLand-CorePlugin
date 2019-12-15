package de.lalaland.core.modules.mobs.custommobs.zombie;

import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.BipipedModel;
import de.lalaland.core.modules.mobs.modeledentities.bipiped.IBiPiped;
import java.util.UUID;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EnumMoveType;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;
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
public class BiPipedZombie extends CustomZombieMob implements IBiPiped {

  private static final int STANDING_TICK_THRESHOLD = 3;
  private static final double MIN_MOVING_DIST = 0.1 * 0.1;

  public BiPipedZombie(final World world, final BipipedModel model, final MobModelManager mobModelManager, final EntityArmorStand token) {
    super(world);
    this.model = model;
    mobModelManager.addModel(this);
    this.token = token;
  }

  private final BipipedModel model;
  private boolean moving = false;
  private int nonMovingTicks = 0;
  private final EntityArmorStand token;

  @Override
  public UUID getEntityID() {
    return uniqueID;
  }

  @Override
  public BipipedModel getModel() {
    return model;
  }

  @Override
  public LivingEntity getBukkit() {
    return getAsBukkitEntity();
  }

  @Override
  public boolean isMoving() {
    return moving;
  }

  @Override
  public ArmorStand getToken() {
    return (ArmorStand) token.getBukkitLivingEntity();
  }

  @Override
  public void tick() {
    nonMovingTicks++;
    if (moving && nonMovingTicks > STANDING_TICK_THRESHOLD) {
      moving = false;
      equipNormal();
    }
    super.tick();
  }

  @Override
  public void move(final EnumMoveType moveType, final Vec3D vec) {
    if (vec.g() >= MIN_MOVING_DIST) {
      nonMovingTicks = 0;
      if (!moving) {
        moving = true;
        equipMoving();
      }
    }
    super.move(moveType, vec);
  }

}
