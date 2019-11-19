package de.lalaland.core.modules.protection.regions;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RegionManager {

  public RegionManager() {
    this.regionChunkCashe = new Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>>();
    this.regionIDCashe = new Object2ObjectOpenHashMap<UUID, ProtectedRegion>();
  }

  private final Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionChunkCashe;
  private final Object2ObjectOpenHashMap<UUID, ProtectedRegion> regionIDCashe;

  public ProtectedRegion getRegionByID(UUID regionID) {
    return regionIDCashe.get(regionID);
  }

  public ProtectedRegion createRegion(Location corner1, Location corner2) {
    return this.createRegion(corner1.getBlock(), corner2.getBlock());
  }

  public ProtectedRegion createRegion(Block corner1, Block corner2) {
    BoundingBox box = BoundingBox.of(corner1, corner2);
    ProtectedRegion region = new ProtectedRegion(box, UUID.randomUUID());
    return region;
  }

  public Set<ProtectedRegion> getRegionsAt(Location location) {
    long chunkKey = Chunk.getChunkKey(location);
    ObjectSet<ProtectedRegion> chunkSet = this.regionChunkCashe.get(chunkKey);
    ObjectOpenHashSet<ProtectedRegion> results = new ObjectOpenHashSet<ProtectedRegion>();
    if (chunkSet != null) {
      results.addAll(chunkSet);
    }
    return results;
  }

  public ProtectedRegion getMostRelevantRegion(Location location) {
    long chunkKey = Chunk.getChunkKey(location);
    ObjectSet<ProtectedRegion> chunkSet = this.regionChunkCashe.get(chunkKey);
    if (chunkSet == null) {
      return null;
    }
    return chunkSet.stream().max((r1, r2) -> r1.getPriority() - r2.getPriority()).get();
  }

}
