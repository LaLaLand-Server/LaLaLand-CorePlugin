package de.lalaland.core.modules.protection.regions;

import de.lalaland.core.modules.protection.ProtectionModule;
import de.lalaland.core.utils.common.UtilChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;
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

  public RegionManager(ProtectionModule protectionModule) {
    this.protectionModule = protectionModule;
    this.regionChunkCashe = new Object2ObjectOpenHashMap<UUID, Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>>>();
    this.regionIDCashe = new Object2ObjectOpenHashMap<UUID, ProtectedRegion>();
  }

  private final Object2ObjectOpenHashMap<UUID, Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>>> regionChunkCashe;
  private final Object2ObjectOpenHashMap<UUID, ProtectedRegion> regionIDCashe;
  private final ProtectionModule protectionModule;

  public ProtectedRegion getRegionByID(UUID regionID) {
    return regionIDCashe.get(regionID);
  }

  public ProtectedRegion createRegion(Location corner1, Location corner2) {
    return this.createRegion(corner1.getBlock(), corner2.getBlock());
  }

  public ProtectedRegion createRegion(UUID regionID, UUID worldID, BoundingBox regionBox) {
    if (!this.regionChunkCashe.containsKey(worldID)) {
      this.regionChunkCashe.put(worldID, new Long2ObjectOpenHashMap<>());
    }
    Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = this.regionChunkCashe
        .get(worldID);
    ProtectedRegion region = new ProtectedRegion(regionBox, regionID, worldID);
    this.regionIDCashe.put(region.getRegionID(), region);
    for (long chunkKey : region.getInheritingChunks()) {
      if (regionCashe.containsKey(chunkKey)) {
        regionCashe.get(chunkKey).add(region);
      } else {
        ObjectOpenHashSet<ProtectedRegion> regionSet = new ObjectOpenHashSet<ProtectedRegion>();
        regionSet.add(region);
        regionCashe.put(chunkKey, regionSet);
      }
    }
    return region;
  }

  public ProtectedRegion createRegion(Block corner1, Block corner2) {
    return this.createRegion(UUID.randomUUID(), corner1.getWorld().getUID(),
        BoundingBox.of(corner1, corner2));
  }

  public Set<ProtectedRegion> getRegionsAt(Location location) {
    long chunkKey = UtilChunk.getChunkKey(location);
    UUID worldID = location.getWorld().getUID();
    ObjectOpenHashSet<ProtectedRegion> results = new ObjectOpenHashSet<ProtectedRegion>();
    Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = this.regionChunkCashe
        .get(worldID);
    if (regionCashe == null) {
      return results;
    }
    ObjectSet<ProtectedRegion> chunkSet = regionCashe.get(chunkKey);
    if (chunkSet != null) {
      results.addAll(chunkSet);
    }
    return results;
  }

  public ProtectedRegion getMostRelevantRegion(Location location) {
    long chunkKey = UtilChunk.getChunkKey(location);
    UUID worldID = location.getWorld().getUID();
    Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = this.regionChunkCashe
        .get(worldID);
    if (regionCashe == null) {
      return null;
    }
    ObjectSet<ProtectedRegion> chunkSet = regionCashe.get(chunkKey);
    if (chunkSet == null) {
      return null;
    }
    return chunkSet.stream()
        .filter(rg -> rg.contains(location))
        .max((r1, r2) -> r1.getPriority() - r2.getPriority())
        .orElseGet(() -> null);
  }

  public void saveRegions(File regionFolder) throws IOException {
    for (ProtectedRegion region : this.regionIDCashe.values()) {
      File regionFile = new File(regionFolder, region.getRegionID().toString() + ".json");
      OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(regionFile));
      osw.write(this.protectionModule.getGson().toJson(region));
      osw.close();
    }
  }

  public void loadRegions(File regionFolder) throws IOException {
    for (File regionFile : regionFolder.listFiles()) {
      InputStreamReader isr = new InputStreamReader(new FileInputStream(regionFile));
      StringBuilder builder = new StringBuilder();
      int read;
      while ((read = isr.read()) != -1) {
        builder.append((char) read);
      }
      this.protectionModule.getGson().fromJson(builder.toString(), ProtectedRegion.class);
    }
  }

}
