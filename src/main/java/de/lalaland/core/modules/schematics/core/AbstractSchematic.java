package de.lalaland.core.modules.schematics.core;

import lombok.Getter;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class AbstractSchematic implements ISchematic {

  public AbstractSchematic(final BoundingBox region, final String schematicID) {
    schmaticID = schematicID;
  }

  @Getter
  private final String schmaticID;

  protected abstract void setBlocks(BoundingBox box);

}
