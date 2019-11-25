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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
public class Schematic extends AbstractSchematic {

  private static final Vector BASE_SCALAR_XZ = new Vector(1, 0, 1);

  public Schematic(final Block lowCorner, final Block highCorner, final String schematicID, final PasteThread pt) {
    super(lowCorner, highCorner, schematicID, pt);
    schematicData = new ObjectOpenHashSet<>();
    iterateAndLoad(lowCorner, highCorner);
  }

  public Schematic(final JsonElement jsonElement, final PasteThread pasteThread) {
    super(jsonElement, pasteThread);
    schematicData = new ObjectOpenHashSet<>();
    final JsonArray array = jsonElement.getAsJsonObject().get("RelativeBlockData").getAsJsonArray();
    for (final JsonElement element : array) {
      schematicData.add(new RelativeBlockData(element.getAsString()));
    }
  }

  private void iterateAndLoad(final Block lowCorner, final Block highCorner) {

    final BoundingBox box = BoundingBox.of(lowCorner, highCorner);
    final World world = lowCorner.getWorld();

    final int lowX = Integer.min(lowCorner.getX(), highCorner.getX());
    final int lowY = Integer.min(lowCorner.getY(), highCorner.getY());
    final int lowZ = Integer.min(lowCorner.getZ(), highCorner.getZ());

    for(double x = box.getMinX(); x < box.getMaxX(); x++) {
      for(double y = box.getMinY(); y < box.getMaxY(); y++) {
        for(double z = box.getMinZ(); z < box.getMaxZ(); z++) {
          final double relX = x - lowX;
          final double relY = y - lowY;
          final double relZ = z - lowZ;
          final BlockData relData = world.getBlockAt((int) x, (int) y, (int) z).getBlockData();
          final RelativeBlockData data = new RelativeBlockData((int) relX, (int) relY, (int) relZ, relData);
          schematicData.add(data);
        }
      }
    }
  }

  private final ObjectSet<RelativeBlockData> schematicData;

  public ObjectSet<RelativeBlockData> getBlockDataClone() {
    return new ObjectOpenHashSet<>(schematicData);
  }

  @Override
  public void pasteCenteredAround(final Location location, final boolean withAir) {
    paste(location.subtract(dimension.clone().multiply(0.5D)), withAir);
  }

  @Override
  public void pasteToGround(final Location location, final boolean withAir) {
    paste(location.subtract(dimension.clone().multiply(BASE_SCALAR_XZ).multiply(0.5D)), withAir);
  }

  @Override
  public JsonElement getAsJson() {
    final JsonObject json = new JsonObject();
    json.addProperty("StringID", schmaticID);
    json.addProperty("Dimension", UtilVect.vecToString(dimension.clone()));
    final JsonArray relativeJson = new JsonArray();
    for (final RelativeBlockData data : schematicData) {
      relativeJson.add(data.getAsString());
    }
    json.add("RelativeBlockData", relativeJson);
    return json;
  }

  @Override
  public void paste(final Location location, final boolean withAir) {
    for (final RelativeBlockData data : schematicData) {
      if (!withAir && data.blockData.getMaterial() == Material.AIR) {
        continue;
      }
      final Location pasteLocation = location.clone().add(data.x, data.y, data.z);
      final PasteJob job = new PasteJob(pasteLocation, data.blockData);
      pasteThread.add(job);
    }
  }

  public void pasteInverted(final Location location, final boolean resetAir) {
    for (final RelativeBlockData data : schematicData) {
      if (!resetAir && data.blockData.getMaterial() == Material.AIR) {
        continue;
      }
      final Location pasteLocation = location.clone().add(data.x, data.y, data.z);
      final PasteJob job = new PasteJob(pasteLocation, Material.AIR.createBlockData());
      pasteThread.add(job);
    }
  }

  public void pasteInvertedCenter(final Location location, final boolean resetAir) {
    pasteInverted(location.subtract(dimension.clone().multiply(0.5D)), resetAir);
  }

  public void pasteInvertedToGround(final Location location, final boolean resetAir) {
    pasteInverted(location.subtract(dimension.clone().multiply(BASE_SCALAR_XZ).multiply(0.5D)), resetAir);
  }

}
