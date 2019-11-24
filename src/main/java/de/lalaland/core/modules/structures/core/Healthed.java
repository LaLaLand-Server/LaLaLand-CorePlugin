package de.lalaland.core.modules.structures.core;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface Healthed {

  public abstract int getMaxHealth();

  public abstract int getCurrentHealth();

  public abstract void removeHealth(int amount);

  public abstract void addHealth(int amount);

  public abstract void onDeath();

}
