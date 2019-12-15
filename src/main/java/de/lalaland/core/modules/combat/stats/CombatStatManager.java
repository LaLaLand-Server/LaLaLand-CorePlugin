package de.lalaland.core.modules.combat.stats;

import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
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
public class CombatStatManager implements Runnable {

  public CombatStatManager(final CorePlugin plugin) {
    this.plugin = plugin;
    plugin.getTaskManager().runRepeatedBukkitAsync(this, 0L, 1L);
    combatStatCalculator = new CombatStatCalculator();
    combatStatEntityMapping = new Object2ObjectOpenHashMap<>();
    scheduledRecalculations = new ObjectOpenHashSet<>();
    for (final World world : Bukkit.getWorlds()) {
      for (final Entity entity : world.getEntities()) {
        if (entity instanceof LivingEntity) {
          initEntity((LivingEntity) entity);
        }
      }
    }
  }

  private final CorePlugin plugin;
  private final CombatStatCalculator combatStatCalculator;
  private final Object2ObjectOpenHashMap<UUID, CombatStatHolder> combatStatEntityMapping;
  private final ObjectOpenHashSet<UUID> scheduledRecalculations;

  /**
   * Gets the mapped {@link CombatStatHolder} of this entity.
   *
   * @param entity
   * @return
   */
  public CombatStatHolder getCombatStatHolder(final LivingEntity entity) {
    return getCombatStatHolder(entity.getUniqueId());
  }

  /**
   * Gets the mapped {@link CombatStatHolder} of this uuid.
   *
   * @param entityID
   * @return
   */
  @Nullable
  public CombatStatHolder getCombatStatHolder(final UUID entityID) {
    final CombatStatHolder holder = combatStatEntityMapping.get(entityID);
    if (holder == null) {
      plugin.getLogger().warning("Tried to request holder of non mapped entity.");
    }
    return holder;
  }

  /**
   * Internal initialisation of a CombatStatHolder.
   *
   * @param entity
   */
  public CombatStatHolder initEntity(final LivingEntity entity) {
    final CombatStatHolder holder =
        combatStatEntityMapping.getOrDefault(entity.getUniqueId(), new CombatStatHolder(entity, combatStatCalculator));
    holder.recalculate();
    combatStatEntityMapping.put(entity.getUniqueId(), holder);
    return holder;
  }

  /**
   * Internal recalculation call for a CombatStatHolder.
   * <p>
   * Recalculation is executed one Tick after calling.
   *
   * @param entityID
   */
  protected void recalculateValues(final UUID entityID) {
    final CombatStatHolder holder = combatStatEntityMapping.get(entityID);
    if (holder == null) {
      plugin.getLogger().warning("Tried to call recalculation on non mapped entity.");
      return;
    }
    if (holder.isRecalculatingSheduled()) {
      return;
    }
    holder.setRecalculatingSheduled(true);
    scheduledRecalculations.add(entityID);
  }

  /**
   * Internal termination of a CombatStatHolder.
   *
   * @param entity
   */
  protected void terminateEntity(final LivingEntity entity) {
    final CombatStatHolder holder = combatStatEntityMapping.get(entity.getUniqueId());
    if (holder == null) {
      plugin.getLogger().warning("Tried to call termination on non mapped entity.");
      return;
    }
    combatStatEntityMapping.remove(entity.getUniqueId());
  }

  @Override
  public void run() {
    for (final UUID schedID : scheduledRecalculations) {
      final CombatStatHolder holder = combatStatEntityMapping.get(schedID);
      if (holder != null) {
        combatStatCalculator.recalculateValues(holder);
        holder.setRecalculatingSheduled(false);
      }
    }
    for (final CombatStatHolder holder : combatStatEntityMapping.values()) {
      holder.tickBuffs();
    }
  }
}
