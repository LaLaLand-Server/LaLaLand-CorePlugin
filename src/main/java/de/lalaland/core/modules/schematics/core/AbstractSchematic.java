package de.lalaland.core.modules.schematics.core;

import de.lalaland.core.modules.schematics.workload.PasteThread;
import lombok.Getter;
import org.bukkit.Location;
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

  public AbstractSchematic(final BoundingBox region, final String schematicID,
      final PasteThread pasteThread) {
    schmaticID = schematicID;
    this.pasteThread = pasteThread;
  }

  @Getter
  private final String schmaticID;
  protected final PasteThread pasteThread;

  public abstract void pasteCenteredAround(Location location);
  public abstract void pastToGround(Location location);
  protected abstract void setBlocks(BoundingBox box);

}
