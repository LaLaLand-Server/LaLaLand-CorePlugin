package de.lalaland.core.modules.protection.regions;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RuleSet {

  public RuleSet() {
    this.rules = Maps.newEnumMap(Relation.class);
    for (Relation rel : Relation.values()) {
      EnumMap<RegionRule, Permit> permits = Maps.newEnumMap(RegionRule.class);
      this.rules.put(rel, permits);
      for (RegionRule rule : RegionRule.values()) {
        permits.put(rule, rule.getDefaultPermit(rel));
      }
    }
  }

  private final EnumMap<Relation, EnumMap<RegionRule, Permit>> rules;
  @Getter
  private boolean isDefaultSet = true;

  public Permit getPermit(Relation relation, RegionRule rule) {
    return this.rules.get(relation).get(rule);
  }

  public void applyRule(Relation relation, RegionRule rule, Permit permit) {
    this.rules.get(relation).put(rule, permit);
    this.isDefaultSet = false;
  }

}
