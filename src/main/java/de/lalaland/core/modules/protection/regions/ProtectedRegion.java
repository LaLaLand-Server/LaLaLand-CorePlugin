package de.lalaland.core.modules.protection.regions;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;
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

  protected ProtectedRegion(BoundingBox region, UUID regionID, UUID worldID) {
    this.worldID = worldID;
    this.regionID = regionID;
    this.boundingBox = region;
    this.ruleSet = new RuleSet();
    this.playerRelations = new Object2ObjectOpenHashMap<UUID, Relation>();
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

  public BoundingBox getBoundingBoxClone() {
    return BoundingBox.of(this.boundingBox.getMin(), this.boundingBox.getMax());
  }

  public Map<UUID, Relation> getRelationsCopy() {
    Map<UUID, Relation> map = Maps.newHashMap();
    map.putAll(this.playerRelations);
    return map;
  }

  public void setRealation(UUID playerID, Relation relation) {
    this.playerRelations.put(playerID, relation);
  }

  public void clearRealation(UUID playerID) {
    this.playerRelations.remove(playerID);
  }

  public Permit getPermit(UUID playerID, RegionRule rule) {
    Relation relation = this.playerRelations.getOrDefault(playerID, Relation.NEUTRAL);
    return this.ruleSet.getPermit(relation, rule);
  }

  public boolean contains(Vector vector) {
    return this.boundingBox.contains(vector);
  }

  public boolean contains(Location location) {
    return this.contains(location.toVector());
  }

  public boolean contains(Block block) {
    return this.contains(block.getLocation());
  }

  public LongSet getInheritingChunks() {
    LongSet chunkKeys = new LongOpenHashSet();

    int minX = ((int) Math.floor(this.boundingBox.getMinX())) >> 4;
    int maxX = ((int) Math.floor(this.boundingBox.getMaxX())) >> 4;
    int minZ = ((int) Math.floor(this.boundingBox.getMinZ())) >> 4;
    int maxZ = ((int) Math.floor(this.boundingBox.getMaxZ())) >> 4;

    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++) {
        chunkKeys.add(Chunk.getChunkKey(x, z));
      }
    }

    return chunkKeys;
  }

}
