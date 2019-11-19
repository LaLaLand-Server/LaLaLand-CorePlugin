package de.lalaland.core.modules.protection.regions;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum RegionRule {

  BLOCK_PLACE(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.DENY)
      .build()),
  BLOCK_BREAK(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.DENY)
      .build()),
  RIGHT_CLICK_BLOCK(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build()),
  INTERACT_AT_ENTITY(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build()),
  OPEN_INVENTORY_BLOCK(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build()),
  DAMAGE_ENTITY(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.ALLOW)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build()),
  DAMAGE_PLAYER(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.ALLOW)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build()),
  DAMAGE_ARMORSTAND(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.DENY)
      .build()),
  PISTON_EXTEND(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.DENY)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.DENY)
      .build()),
  BLOCK_EXPLODE(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.DENY)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.DENY)
      .build()),
  ITEM_PICKUP(ImmutableMap.<Relation, Permit>builder()
      .put(Relation.FRIENDLY, Permit.ALLOW)
      .put(Relation.HOSTILE, Permit.DENY)
      .put(Relation.NEUTRAL, Permit.ALLOW)
      .build());

  private final ImmutableMap<Relation, Permit> defaultPermits;

  public Permit getDefaultPermit(Relation relation) {
    return defaultPermits.getOrDefault(relation, Permit.DENY);
  }

}
