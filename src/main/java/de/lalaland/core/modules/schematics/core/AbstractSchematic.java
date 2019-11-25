package de.lalaland.core.modules.schematics.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.lalaland.core.modules.schematics.workload.PasteThread;
import de.lalaland.core.utils.common.UtilVect;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
public abstract class AbstractSchematic implements ISchematic {

  public AbstractSchematic(final Block lowCorner, final Block highCorner, final String schematicID,
      final PasteThread pasteThread) {
    schmaticID = schematicID;
    this.pasteThread = pasteThread;
    final BoundingBox region = BoundingBox.of(lowCorner, highCorner);
    dimension = region.getMax().clone().subtract(region.getMin());
  }

  public AbstractSchematic(final JsonElement jsonElement, final PasteThread pasteThread) {
    final JsonObject json = jsonElement.getAsJsonObject();
    schmaticID = json.get("StringID").getAsString();
    this.pasteThread = pasteThread;
    dimension = UtilVect.vecFromString(json.get("Dimension").getAsString());
  }

  @Getter
  protected final String schmaticID;
  protected final PasteThread pasteThread;

  protected final Vector dimension;

  public abstract void pasteCenteredAround(Location location, boolean withAir);

  public abstract void pasteToGround(Location location, boolean withAir);

  public abstract JsonElement getAsJson();

  @Override
  public Vector getDimension() {
    return dimension.clone();
  }

}
