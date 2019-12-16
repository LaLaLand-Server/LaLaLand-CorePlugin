package de.lalaland.core.modules.combat.stats;

import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

  private static final long TICKS_PER_MANA = 4L;

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

  private long manaTickCounter;
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
    if (entity instanceof Player) {
      loadHolder(holder, entity.getUniqueId());
    }
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
    if (entity instanceof Player) {
      saveHolder(holder, entity.getUniqueId());
    }
    combatStatEntityMapping.remove(entity.getUniqueId());
  }

  private void saveHolder(final CombatStatHolder holder, final UUID entityID) {
    final File playerSkillFolder = new File(plugin.getDataFolder() + File.separator + "skilldata");
    if (!playerSkillFolder.exists()) {
      playerSkillFolder.mkdirs();
    }
    final String jsonString = plugin.getGson().toJson(holder.serializeSkills());
    final File skillFile = new File(playerSkillFolder, entityID.toString() + ".json");
    try {
      FileUtils.write(skillFile, jsonString, "UTF-8");
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void loadHolder(final CombatStatHolder holder, final UUID entityID) {
    final File playerSkillFolder = new File(plugin.getDataFolder() + File.separator + "skilldata");
    if (!playerSkillFolder.exists()) {
      return;
    }
    final File skillFile = new File(playerSkillFolder, entityID.toString() + ".json");
    if (!skillFile.exists()) {
      return;
    }
    try {
      final JsonObject json = plugin.getGson().fromJson(FileUtils.readFileToString(skillFile, "UTF-8"), JsonObject.class);
      if (json != null) {
        holder.deserializeSkills(json);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    final boolean isManaFillupTime = ++manaTickCounter % TICKS_PER_MANA == 0;
    for (final UUID schedID : scheduledRecalculations) {
      final CombatStatHolder holder = combatStatEntityMapping.get(schedID);
      if (holder != null) {
        combatStatCalculator.recalculateValues(holder);
        holder.setRecalculatingSheduled(false);
      }
    }
    for (final CombatStatHolder holder : combatStatEntityMapping.values()) {
      holder.tickBuffs();
      if (isManaFillupTime) {
        holder.addMana(1);
      }
    }
  }
}
