package de.lalaland.core.modules.protection.regions;

import com.google.common.collect.Maps;
import de.lalaland.core.modules.schematics.core.Schematic;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 18.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ProtectedRegion {

  protected ProtectedRegion(final BoundingBox region, final UUID regionID, final UUID worldID) {
    this.worldID = worldID;
    this.regionID = regionID;
    boundingBox = region;
    ruleSet = new RuleSet();
    playerRelations = new Object2ObjectOpenHashMap<>();
  }

  @Getter
  @Setter
  private String name = "NO_NAME";
  private final BoundingBox boundingBox;
  @Getter
  private final UUID regionID;
  @Getter
  @Setter
  private int priority;
  @Getter
  @Setter
  private RuleSet ruleSet;
  private final Object2ObjectOpenHashMap<UUID, Relation> playerRelations;
  @Getter
  private final UUID worldID;

  public Location getCenter() {
    final World world = Bukkit.getWorld(worldID);
    final Vector center = boundingBox.getCenter();
    return new Location(world, center.getX(), center.getY(), center.getZ());
  }

  public Location getGroundCenter() {
    final World world = Bukkit.getWorld(worldID);
    final double halfSize = boundingBox.getHeight() * 0.5;
    final Vector offset = new Vector(0, halfSize - 0.1, 0);
    final Vector lowCenter = boundingBox.getCenter().clone().subtract(offset);
    return new Location(world, lowCenter.getX(), lowCenter.getY(), lowCenter.getZ());
  }

  public BoundingBox getBoundingBoxClone() {
    return BoundingBox.of(boundingBox.getMin(), boundingBox.getMax());
  }

  public Vector getDimension() {
    return boundingBox.getMax().clone().subtract(boundingBox.getMin());
  }

  public boolean isSchematicViable(final Schematic schematic) {
    final Vector svec = schematic.getDimension();
    final Vector rvec = getDimension();
    final double sx = svec.getX();
    final double sy = svec.getY();
    final double sz = svec.getZ();
    final double rx = rvec.getX();
    final double ry = rvec.getY();
    final double rz = rvec.getZ();
    return sx <= rx && sy <= ry && sz <= rz;
  }

  public Map<UUID, Relation> getRelationsCopy() {
    final Map<UUID, Relation> map = Maps.newHashMap();
    map.putAll(playerRelations);
    return map;
  }

  public void setRealation(final UUID playerID, final Relation relation) {
    playerRelations.put(playerID, relation);
  }

  public void clearRealation(final UUID playerID) {
    playerRelations.remove(playerID);
  }

  public Permit getPermit(final UUID playerID, final RegionRule rule) {
    final Relation relation = playerRelations.getOrDefault(playerID, Relation.NEUTRAL);
    return ruleSet.getPermit(relation, rule);
  }

  public boolean contains(final Vector vector) {
    return boundingBox.contains(vector);
  }

  public boolean contains(final Location location) {
    return contains(location.toVector());
  }

  public boolean contains(final Block block) {
    return contains(block.getLocation());
  }

  public LongSet getInheritingChunks() {
    final LongSet chunkKeys = new LongOpenHashSet();

    final int minX = ((int) Math.floor(boundingBox.getMinX())) >> 4;
    final int maxX = ((int) Math.floor(boundingBox.getMaxX())) >> 4;
    final int minZ = ((int) Math.floor(boundingBox.getMinZ())) >> 4;
    final int maxZ = ((int) Math.floor(boundingBox.getMaxZ())) >> 4;

    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++) {
        chunkKeys.add(Chunk.getChunkKey(x, z));
      }
    }

    return chunkKeys;
  }

}
