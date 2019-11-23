package de.lalaland.core.modules.schematics.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.lalaland.core.modules.schematics.workload.PasteJob;
import de.lalaland.core.modules.schematics.workload.PasteThread;
import de.lalaland.core.utils.common.UtilVect;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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

  private static final Vector BASE_SCALAR_XZ = new Vector(1, 0, 1);

  public SimpleSchematic(final Block lowCorner, final Block highCorner, final String schematicID,
      final PasteThread pt) {
    super(lowCorner, highCorner, schematicID, pt);
    schematicData = new ObjectOpenHashSet<>();
    iterateAndLoad(lowCorner, highCorner);
  }

  private void iterateAndLoad(final Block lowCorner, final Block highCorner) {
    final int baseX = lowCorner.getX();
    final int baseY = lowCorner.getY();
    final int baseZ = lowCorner.getZ();
    for (int x = baseX; x <= highCorner.getX(); x++) {
      for (int y = baseY; y <= highCorner.getY(); y++) {
        for (int z = baseZ; z <= highCorner.getZ(); z++) {
          final int relX = x - baseX;
          final int relY = y - baseY;
          final int relZ = z - baseZ;
          final BlockData relData = lowCorner.getRelative(relX, relY, relZ).getBlockData();
          final RelativeBlockData data = new RelativeBlockData(relX, relY, relZ, relData);
          schematicData.add(data);
        }
      }
    }
  }

  public SimpleSchematic(final JsonElement jsonElement, final PasteThread pasteThread) {
    super(jsonElement, pasteThread);
    schematicData = new ObjectOpenHashSet<>();
    final JsonArray array = jsonElement.getAsJsonObject().get("RelativeBlockData").getAsJsonArray();
    for (final JsonElement element : array) {
      schematicData.add(new RelativeBlockData(element.getAsString()));
    }
  }

  private final ObjectSet<RelativeBlockData> schematicData;

  @Override
  public void pasteCenteredAround(final Location location) {
    paste(location.subtract(dimension.multiply(0.5D)));
  }

  @Override
  public void pasteToGround(final Location location) {
    paste(location.subtract(dimension.multiply(BASE_SCALAR_XZ).multiply(0.5D)));
  }

  @Override
  public JsonElement getAsJson() {
    final JsonObject json = new JsonObject();
    json.addProperty("StringID", schmaticID);
    json.addProperty("Dimension", UtilVect.vecToString(dimension));
    final JsonArray relativeJson = new JsonArray();
    for (final RelativeBlockData data : schematicData) {
      relativeJson.add(data.getAsString());
    }
    json.add("RelativeBlockData", relativeJson);
    return json;
  }

  @Override
  public void paste(final Location location) {
    for (final RelativeBlockData data : schematicData) {
      final Location pasteLocation = location.clone().add(data.x, data.y, data.z);
      final PasteJob job = new PasteJob(pasteLocation, data.blockData);
      pasteThread.add(job);
    }
  }

}
