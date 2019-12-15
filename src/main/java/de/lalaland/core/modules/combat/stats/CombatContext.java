package de.lalaland.core.modules.combat.stats;

import lombok.Data;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
public class CombatContext {

  @Nullable
  private CombatStatHolder attackerHolder = null;
  private CombatStatHolder defenderHolder = null;
  private DamageCause cause;
  private double damage;
  private boolean crit;
  private boolean ranged;

}
