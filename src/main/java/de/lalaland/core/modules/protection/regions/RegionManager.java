package de.lalaland.core.modules.protection.regions;

import de.lalaland.core.modules.protection.ProtectionModule;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.utils.common.UtilChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

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

  public RegionManager(final ProtectionModule protectionModule) {
    this.protectionModule = protectionModule;
    regionChunkCashe = new Object2ObjectOpenHashMap<>();
    regionIDCashe = new Object2ObjectOpenHashMap<>();
  }

  private final Object2ObjectOpenHashMap<UUID, Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>>> regionChunkCashe;
  private final Object2ObjectOpenHashMap<UUID, ProtectedRegion> regionIDCashe;
  private final ProtectionModule protectionModule;

  public ProtectedRegion getRegionByID(final UUID regionID) {
    return regionIDCashe.get(regionID);
  }

  public void removeRegion(final ProtectedRegion region) {
    regionIDCashe.remove(region.getRegionID());
    final Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> chunkMap = regionChunkCashe.get(region.getWorldID());
    final LongSet emptyChunks = new LongOpenHashSet();
    for (final long chunkKey : region.getInheritingChunks()) {
      final ObjectSet<ProtectedRegion> regionSet = chunkMap.get(chunkKey);
      regionSet.remove(region);
      if (regionSet.isEmpty()) {
        emptyChunks.add(chunkKey);
      }
    }
    for (final long emptyKey : emptyChunks) {
      chunkMap.remove(emptyKey);
    }
    if (chunkMap.isEmpty()) {
      regionChunkCashe.remove(region.getWorldID());
    }
  }

  // TODO check if vector 1 1 0 or 1 0 1 for ground scalar
  public ProtectedRegion createFor(final Schematic schematic, final Location groundLocation) {
    final World world = groundLocation.getWorld();
    Vector dimension = schematic.getDimension();
    dimension = dimension.subtract(new Vector(1, 1, 1));

    final Vector offset = dimension.clone().multiply(new Vector(1, 0, 1)).multiply(0.5);

    final Vector min = groundLocation.clone().toVector().subtract(offset);
    final Vector max = min.clone().add(dimension);

    final Location minLoc = new Location(world, min.getX(), min.getY(), min.getZ());
    final Location maxLoc = new Location(world, max.getX(), max.getY(), max.getZ());
    return createRegion(minLoc, maxLoc);
  }

  public ProtectedRegion createRegion(final Location corner1, final Location corner2) {
    return createRegion(corner1.getBlock(), corner2.getBlock());
  }

  public ProtectedRegion createRegion(final UUID regionID, final UUID worldID, final BoundingBox regionBox) {
    if (!regionChunkCashe.containsKey(worldID)) {
      regionChunkCashe.put(worldID, new Long2ObjectOpenHashMap<>());
    }
    final Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = regionChunkCashe
        .get(worldID);
    final ProtectedRegion region = new ProtectedRegion(regionBox, regionID, worldID);
    regionIDCashe.put(region.getRegionID(), region);
    for (final long chunkKey : region.getInheritingChunks()) {
      if (regionCashe.containsKey(chunkKey)) {
        regionCashe.get(chunkKey).add(region);
      } else {
        final ObjectOpenHashSet<ProtectedRegion> regionSet = new ObjectOpenHashSet<>();
        regionSet.add(region);
        regionCashe.put(chunkKey, regionSet);
      }
    }
    return region;
  }

  public ProtectedRegion createRegion(final Block corner1, final Block corner2) {
    return createRegion(UUID.randomUUID(), corner1.getWorld().getUID(),
        BoundingBox.of(corner1, corner2));
  }

  public Set<ProtectedRegion> getRegionsAt(final Location location) {
    final long chunkKey = UtilChunk.getChunkKey(location);
    final UUID worldID = location.getWorld().getUID();
    final ObjectOpenHashSet<ProtectedRegion> results = new ObjectOpenHashSet<>();
    final Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = regionChunkCashe
        .get(worldID);
    if (regionCashe == null) {
      return results;
    }
    final ObjectSet<ProtectedRegion> chunkSet = regionCashe.get(chunkKey);
    if (chunkSet != null) {
      results.addAll(chunkSet);
    }
    return results;
  }

  public ProtectedRegion getMostRelevantRegion(final Location location) {
    final long chunkKey = UtilChunk.getChunkKey(location);
    final UUID worldID = location.getWorld().getUID();
    final Long2ObjectOpenHashMap<ObjectSet<ProtectedRegion>> regionCashe = regionChunkCashe
        .get(worldID);
    if (regionCashe == null) {
      return null;
    }
    final ObjectSet<ProtectedRegion> chunkSet = regionCashe.get(chunkKey);
    if (chunkSet == null) {
      return null;
    }
    return chunkSet.stream()
        .filter(rg -> rg.contains(location))
        .max((r1, r2) -> r1.getPriority() - r2.getPriority())
        .orElseGet(() -> null);
  }

  public void saveRegions(final File regionFolder) throws IOException {
    for (final ProtectedRegion region : regionIDCashe.values()) {
      final File regionFile = new File(regionFolder, region.getRegionID().toString() + ".json");
      final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(regionFile));
      osw.write(protectionModule.getGson().toJson(region));
      osw.close();
    }
  }

  public void loadRegions(final File regionFolder) throws IOException {
    for (final File regionFile : regionFolder.listFiles()) {
      final InputStreamReader isr = new InputStreamReader(new FileInputStream(regionFile));
      final StringBuilder builder = new StringBuilder();
      int read;
      while ((read = isr.read()) != -1) {
        builder.append((char) read);
      }
      protectionModule.getGson().fromJson(builder.toString(), ProtectedRegion.class);
    }
  }

}
