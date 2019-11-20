package de.lalaland.core.modules.protection.regions.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.utils.common.UtilVect;
import java.lang.reflect.Type;
import java.util.Map;
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
public class ProtectedRegionSerializer implements JsonSerializer<ProtectedRegion> {

  @Override
  public JsonElement serialize(ProtectedRegion protectedRegion, Type type,
      JsonSerializationContext jsonSerializationContext) {

    JsonObject json = new JsonObject();

    String worldID = protectedRegion.getWorldID().toString();
    String id = protectedRegion.getRegionID().toString();
    BoundingBox box = protectedRegion.getBoundingBoxClone();
    String min = UtilVect.vecToString(box.getMin());
    String max = UtilVect.vecToString(box.getMax());
    String name = protectedRegion.getName();
    int priority = protectedRegion.getPriority();
    JsonElement ruleSet = jsonSerializationContext.serialize(protectedRegion.getRuleSet());
    JsonObject relationMap = new JsonObject();

    Map<UUID, Relation> relations = protectedRegion.getRelationsCopy();

    for (Entry<UUID, Relation> entry : relations.entrySet()) {
      relationMap.addProperty(entry.getKey().toString(), entry.getValue().toString());
    }

    json.addProperty("UUID", id);
    json.addProperty("WorldID", worldID);
    json.addProperty("MinVec", min);
    json.addProperty("MaxVec", max);
    json.addProperty("Name", name);
    json.addProperty("Priority", priority);
    json.add("RuleSet", ruleSet);
    json.add("RelationMap", relationMap);

    return json;
  }
}
