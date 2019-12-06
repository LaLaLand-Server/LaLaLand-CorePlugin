package de.lalaland.core.modules.protection.zones;

import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 06.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WorldZoneManager {

  public WorldZoneManager(final RegionManager regionManager) {
    zoneMappings = new Object2ObjectOpenHashMap<>();
  }

  private final Object2ObjectMap<ProtectedRegion, WorldZone> zoneMappings;

  public WorldZone getZoneOf(final ProtectedRegion region) {
    return zoneMappings.get(region);
  }

  public void init(final RegionManager regionManager) {
    for (final WorldZone zone : WorldZone.values()) {
      final UUID zoneID = zone.getRegionID();
      if (zoneID == null) {
        continue;
      }
      final ProtectedRegion region = regionManager.getRegionByID(zoneID);
      zoneMappings.put(region, zone);
    }
  }

}
