package de.lalaland.core.modules.schematics.core;

import de.lalaland.core.modules.schematics.workload.PasteJob;
import de.lalaland.core.modules.schematics.workload.PasteThread;
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

  public SimpleSchematic(final BoundingBox region, final String schematicID, final PasteThread pt) {
    super(region, schematicID, pt);
    schematicData = new ObjectOpenHashSet<>();
  }



  private final ObjectSet<RelativeBlockData> schematicData;

  @Override
  protected void setBlocks(final BoundingBox box) {

  }

  @Override
  public void pasteCenteredAround(final Location location) {

  }

  @Override
  public void pastToGround(final Location location) {

  }

  @Override
  public void paste(final Location location) {
    for (final RelativeBlockData data : schematicData) {
      final Location pasteLocation = location.clone().add(data.x, data.y, data.z);
      final PasteJob job = new PasteJob(pasteLocation, data.blockData);
      pasteThread.add(job);
    }
  }

  @Override
  public Vector getDimension() {
    return null;
  }
}
