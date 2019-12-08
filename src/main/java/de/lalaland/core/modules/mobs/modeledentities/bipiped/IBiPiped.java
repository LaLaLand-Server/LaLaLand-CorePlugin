package de.lalaland.core.modules.mobs.modeledentities.bipiped;

import de.lalaland.core.modules.mobs.modeledentities.ComplexModel;
import org.bukkit.inventory.EntityEquipment;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface IBiPiped extends ComplexModel<BipipedModel> {

  @Override
  public abstract BipipedModel getModel();

  @Override
  public default void equipNormal() {
    final BiModelEquipment equip = getModel().getEquipment();
    final EntityEquipment inv = getBukkit().getEquipment();
    inv.setHelmet(equip.getHeadItem());
    inv.setItemInMainHand(equip.getRightHand());
    inv.setItemInOffHand(equip.getLeftHand());
    getToken().setHelmet(equip.getTorso());
  }

  @Override
  public default void equipMoving() {
    final BiModelEquipment equip = getModel().getEquipment();
    final EntityEquipment inv = getBukkit().getEquipment();
    inv.setHelmet(equip.getHeadItem());
    inv.setItemInMainHand(equip.getRightHand());
    inv.setItemInOffHand(equip.getLeftHand());
    getToken().setHelmet(equip.getTorso_moving());
  }

  @Override
  public default void equipHurt() {
    final BiModelEquipment equip = getModel().getEquipment();
    final EntityEquipment inv = getBukkit().getEquipment();
    if (isMoving()) {
      inv.setHelmet(equip.getHeadItem_hurt());
      inv.setItemInMainHand(equip.getRightHand_hurt());
      inv.setItemInOffHand(equip.getLeftHand_hurt());
      getToken().setHelmet(equip.getTorso_moving_hurt());
    } else {
      inv.setHelmet(equip.getHeadItem_hurt());
      inv.setItemInMainHand(equip.getRightHand_hurt());
      inv.setItemInOffHand(equip.getLeftHand_hurt());
      getToken().setHelmet(equip.getTorso_hurt());
    }
  }

  @Override
  public default void equipAttack() {
  }

  @Override
  public default int getAttackFrames() {
    return 0;
  }

}
