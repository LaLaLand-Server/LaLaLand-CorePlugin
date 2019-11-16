package de.lalaland.core.modules.CombatModule.stats;

import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatStatManager {

  public CombatStatManager(CorePlugin plugin) {
    this.combatStatCalculator = new CombatStatCalculator();
    this.combatStatEntityMapping = new Object2ObjectOpenHashMap<>();
  }

  private final CombatStatCalculator combatStatCalculator;
  private final Object2ObjectOpenHashMap<UUID, CombatStatHolder> combatStatEntityMapping;

}
