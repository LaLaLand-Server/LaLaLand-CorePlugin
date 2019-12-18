package de.lalaland.core.modules.combat.stats;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.UserManager;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
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

  public static final long TICKS_PER_MANA = 6L;
  public static final int MANA_PER_UPDATE = 1;
  protected static final int LEVEL_HARD_CAP = 50;

  public CombatStatManager(final CorePlugin plugin) {
    this.plugin = plugin;
    plugin.getTaskManager().runRepeatedBukkitAsync(this, 0L, 1L);
    combatStatCalculator = new CombatStatCalculator();
    combatStatEntityMapping = new Object2ObjectOpenHashMap<>();
    scheduledRecalculations = new ObjectOpenHashSet<>();
    expToLevelMap = TreeRangeMap.create();
    levelToExpMap = new Int2LongOpenHashMap();
    generateExpMaps();
    for (final World world : Bukkit.getWorlds()) {
      for (final Entity entity : world.getEntities()) {
        if (entity instanceof LivingEntity) {
          initEntity((LivingEntity) entity);
        }
      }
    }
  }

  private final CorePlugin plugin;
  @Getter(AccessLevel.PROTECTED)
  private final CombatStatCalculator combatStatCalculator;
  private final Object2ObjectOpenHashMap<UUID, CombatStatHolder> combatStatEntityMapping;
  private final ObjectOpenHashSet<UUID> scheduledRecalculations;
  private final RangeMap<Long, Integer> expToLevelMap;
  private final Int2LongMap levelToExpMap;

  protected int getMaxLevelOfExp(final long exp) {
    return expToLevelMap.get(exp);
  }

  protected long getMinExpOfLevel(final int level) {
    if (level < 0 || level > LEVEL_HARD_CAP) {
      throw new IndexOutOfBoundsException("Level must be between " + 1 + " and " + LEVEL_HARD_CAP + ", including both.");
    }
    return levelToExpMap.get(level);
  }

  private void generateExpMaps() {

    levelToExpMap.put(0, 0);
    levelToExpMap.put(1, 0);

    long sum = 0;

    for (int lvl = 1; lvl <= LEVEL_HARD_CAP; lvl++) {
      final long deltaExp = (long) evalExp(lvl + 1);
      if (lvl != LEVEL_HARD_CAP) {
        expToLevelMap.put(Range.closed(sum, (sum + deltaExp) - 1), lvl);
      } else {
        expToLevelMap.put(Range.atLeast(sum), lvl);
      }
      levelToExpMap.put(lvl, sum);
      sum += deltaExp;
    }
  }

  private double evalExp(final int lvl) {
    return (lvl - 1D) + ((exponentialRationalNumerator(lvl) / exponentialRationalDenominator(lvl)));
  }

  private double exponentialRationalNumerator(final int lvl) {
    return 75D * ((Math.pow(2D, (lvl / 7D) - (1D / 7D))) - 1D);
  }

  private double exponentialRationalDenominator(final int lvl) {
    return 1D - ((1D / 2D) * Math.pow(2, 6D / 7D));
  }

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
        combatStatEntityMapping.getOrDefault(entity.getUniqueId(), new CombatStatHolder(entity, this));
    holder.recalculate();
    combatStatEntityMapping.put(entity.getUniqueId(), holder);
    if (entity instanceof Player) {
      loadHolder(holder, entity.getUniqueId());
      UserManager.getInstance().getUser(entity.getUniqueId()).setCombatStatHolder(holder);
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
    final File combatStatFolder = new File(plugin.getDataFolder() + File.separator + "combatstats");
    if (!combatStatFolder.exists()) {
      combatStatFolder.mkdirs();
    }
    final String jsonString = plugin.getGson().toJson(holder.serialize());
    final File combatFile = new File(combatStatFolder, entityID.toString() + ".json");
    try {
      FileUtils.write(combatFile, jsonString, "UTF-8");
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void loadHolder(final CombatStatHolder holder, final UUID entityID) {
    final File combatStatFolder = new File(plugin.getDataFolder() + File.separator + "combatstats");
    if (!combatStatFolder.exists()) {
      return;
    }
    final File combatFile = new File(combatStatFolder, entityID.toString() + ".json");
    if (!combatFile.exists()) {
      return;
    }
    try {
      final JsonObject json = plugin.getGson().fromJson(FileUtils.readFileToString(combatFile, "UTF-8"), JsonObject.class);
      if (json != null) {
        holder.deserialize(json);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
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
    scheduledRecalculations.clear();
    for (final CombatStatHolder holder : combatStatEntityMapping.values()) {
      holder.tickBuffs();
      holder.tickMana();
    }
  }
}
