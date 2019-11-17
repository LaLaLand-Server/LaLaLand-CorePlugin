package de.lalaland.core.modules.CombatModule.stats;

import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

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
    this.plugin = plugin;
    this.combatStatCalculator = new CombatStatCalculator();
    this.combatStatEntityMapping = new Object2ObjectOpenHashMap<>();
    for (World world : Bukkit.getWorlds()) {
      for (Entity entity : world.getEntities()) {
        if (entity instanceof LivingEntity) {
          this.initEntity((LivingEntity) entity);
        }
      }
    }
  }

  private final CorePlugin plugin;
  private final CombatStatCalculator combatStatCalculator;
  private final Object2ObjectOpenHashMap<UUID, CombatStatHolder> combatStatEntityMapping;

  /**
   * Gets the mapped {@link CombatStatHolder} of this entity.
   *
   * @param entity
   * @return
   */
  public CombatStatHolder getCombatStatHolder(LivingEntity entity) {
    return this.getCombatStatHolder(entity.getUniqueId());
  }

  /**
   * Gets the mapped {@link CombatStatHolder} of this uuid.
   *
   * @param entityID
   * @return
   */
  @Nullable
  public CombatStatHolder getCombatStatHolder(UUID entityID) {
    CombatStatHolder holder = this.combatStatEntityMapping.get(entityID);
    if (holder == null) {
      this.plugin.getLogger().warning("Tried to request holder of non mapped entity.");
    }
    return holder;
  }

  /**
   * Internal initialisation of a CombatStatHolder.
   *
   * @param entity
   */
  protected void initEntity(LivingEntity entity) {
    CombatStatHolder holder = new CombatStatHolder(entity);
    combatStatCalculator.recalculateValues(holder);
    this.combatStatEntityMapping.put(entity.getUniqueId(), holder);
  }

  /**
   * Internal recalculation call for a CombatStatHolder.
   *
   * @param entityID
   */
  protected void recalculateValues(UUID entityID) {
    CombatStatHolder holder = this.combatStatEntityMapping.get(entityID);
    if (holder == null) {
      plugin.getLogger().warning("Tried to call recalculation on non mapped entity.");
      return;
    }
    this.combatStatCalculator.recalculateValues(holder);
  }

  /**
   * Internal termination of a CombatStatHolder.
   *
   * @param entity
   */
  protected void terminateEntity(LivingEntity entity) {
    CombatStatHolder holder = this.combatStatEntityMapping.get(entity.getUniqueId());
    if (holder == null) {
      plugin.getLogger().warning("Tried to call termination on non mapped entity.");
      return;
    }
    this.combatStatEntityMapping.remove(entity.getUniqueId());
  }

}
