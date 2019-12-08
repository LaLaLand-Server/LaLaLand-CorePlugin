package de.lalaland.core.modules.mobs.modeledentities.singlemodel;

import de.lalaland.core.modules.mobs.modeledentities.ComplexModel;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface ISingleModel extends ComplexModel<SingleModel> {

  @Override
  public abstract SingleModel getModel();

  @Override
  public default void equipNormal() {
    final SingleModel model = getModel();
    getToken().setHelmet(model.getSingleModelEquipment().getIdleItem());
  }

  @Override
  public default void equipMoving() {
    final SingleModel model = getModel();
    getToken().setHelmet(model.getSingleModelEquipment().getMoveItem());
  }

  @Override
  public default void equipHurt() {
    final SingleModel model = getModel();
    if (isMoving()) {
      getToken().setHelmet(model.getSingleModelEquipment().getMoveHurtItem());
    } else {
      getToken().setHelmet(model.getSingleModelEquipment().getIdleHurtItem());
    }
  }

  public default void equipAttack() {
    final SingleModel model = getModel();
    getToken().setHelmet(model.getSingleModelEquipment().getAttackItem());
  }

}
