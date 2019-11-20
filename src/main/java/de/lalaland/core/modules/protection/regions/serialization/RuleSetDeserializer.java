package de.lalaland.core.modules.protection.regions.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.lalaland.core.modules.protection.regions.Permit;
import de.lalaland.core.modules.protection.regions.RegionRule;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.modules.protection.regions.RuleSet;
import java.lang.reflect.Type;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RuleSetDeserializer implements JsonDeserializer<RuleSet> {

  @Override
  public RuleSet deserialize(final JsonElement jsonElement, final Type typeOfT,
      final JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject json = jsonElement.getAsJsonObject();
    final RuleSet ruleSet = new RuleSet();
    final boolean isDefault = json.get("IsDefaultSet").getAsBoolean();
    if (isDefault) {
      return ruleSet;
    }

    final JsonObject ruleEntries = json.get("RuleEntries").getAsJsonObject();

    for (final Entry<String, JsonElement> relationEntry : ruleEntries.entrySet()) {
      final Relation relation = Relation.valueOf(relationEntry.getKey());
      final JsonObject relationObject = relationEntry.getValue().getAsJsonObject();
      for (final Entry<String, JsonElement> ruleEntry : relationObject.entrySet()) {
        ruleSet.applyRule(relation, RegionRule.valueOf(ruleEntry.getKey()),
            Permit.valueOf(ruleEntry.getValue().getAsString()));
      }
    }

    return ruleSet;
  }
}
