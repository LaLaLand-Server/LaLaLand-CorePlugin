package de.lalaland.core.modules.schematics.core;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 22.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SimpleSchematic extends AbstractSchematic {

  public SimpleSchematic(final BoundingBox region, final String schematicID) {
    super(region, schematicID);
    schematicData = new ObjectOpenHashSet<>();
  }

  @Override
  protected void setBlocks(final BoundingBox box) {

  }

  private final ObjectSet<RelativeBlockData> schematicData;

  @Override
  public void paste(final Location location) {

  }

  @Override
  public void pasteWithMiddle(final Location location) {

  }

  @Override
  public Vector getDimension() {
    return null;
  }
}
