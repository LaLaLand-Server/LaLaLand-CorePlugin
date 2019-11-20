package de.lalaland.core.modules.protection.regions.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.modules.protection.regions.RuleSet;
import de.lalaland.core.utils.common.UtilVect;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.UUID;
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
public class ProtectedRegionDeserializer implements JsonDeserializer<ProtectedRegion> {

  public ProtectedRegionDeserializer(RegionManager regionManager) {
    this.regionManager = regionManager;
  }

  private final RegionManager regionManager;

  @Override
  public ProtectedRegion deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    JsonObject json = jsonElement.getAsJsonObject();
    UUID regionID = UUID.fromString(json.get("UUID").getAsString());
    UUID worldID = UUID.fromString(json.get("WorldID").getAsString());

    Vector min = UtilVect.vecFromString(json.get("MinVec").getAsString());
    Vector max = UtilVect.vecFromString(json.get("MaxVec").getAsString());
    BoundingBox regionBox = BoundingBox.of(min, max);

    ProtectedRegion region = regionManager.createRegion(regionID, worldID, regionBox);

    String name = json.get("Name").getAsString();
    int priority = json.get("Priority").getAsInt();
    RuleSet ruleSet = jsonDeserializationContext
        .deserialize(json.get("RuleSet").getAsJsonObject(), RuleSet.class);
    region.setRuleSet(ruleSet);
    JsonObject relationMap = json.get("RelationMap").getAsJsonObject();
    for (Entry<String, JsonElement> entry : relationMap.entrySet()) {
      UUID playerID = UUID.fromString(entry.getKey());
      Relation relation = Relation.valueOf(entry.getValue().getAsString());
      region.setRealation(playerID, relation);
    }

    region.setName(name);
    region.setPriority(priority);

    return region;
  }
}
