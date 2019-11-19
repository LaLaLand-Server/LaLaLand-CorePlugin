package de.lalaland.core.modules.protection.regions.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.lalaland.core.modules.protection.regions.RegionRule;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.modules.protection.regions.RuleSet;
import java.lang.reflect.Type;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RuleSetSerializer implements JsonSerializer<RuleSet> {

  @Override
  public JsonElement serialize(RuleSet src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject json = new JsonObject();
    json.addProperty("IsDefaultSet", src.isDefaultSet());
    for (Relation relation : Relation.values()) {
      JsonObject relationObject = new JsonObject();
      for (RegionRule rule : RegionRule.values()) {
        relationObject.addProperty(rule.toString(), src.getPermit(relation, rule).toString());
      }
      json.add(relation.toString(), relationObject);
    }
    return json;
  }
}
