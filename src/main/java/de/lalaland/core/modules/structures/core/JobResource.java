package de.lalaland.core.modules.structures.core;

import com.google.common.base.Preconditions;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.ui.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 27.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobResource extends Structure implements Healthed {

  public JobResource(final ProtectedRegion protectedRegion,
      final Schematic baseSchematic,
      final Schematic emptySchematic,
      final StructureManager structureManager,
      final int maxHealth,
      final JobType jobType,
      final int jobLevel,
      final long respawnTimeDelta,
      final JobDataManager jobDataManager,
      final Resource resource) {
    super(protectedRegion, baseSchematic, structureManager);

    this.jobType = jobType;
    this.jobLevel = jobLevel;
    this.maxHealth = maxHealth;
    currentHealth = maxHealth;
    this.emptySchematic = emptySchematic;
    this.respawnTimeDelta = respawnTimeDelta;
    this.jobDataManager = jobDataManager;
    this.resource = resource;
    Preconditions.checkArgument(emptySchematic.getDimension().equals(baseSchematic.getDimension()));
  }

  private final JobDataManager jobDataManager;
  private final JobType jobType;
  private final int jobLevel;
  private final int maxHealth;
  private final Schematic emptySchematic;
  private int currentHealth;
  private final long respawnTimeDelta;
  private final Resource resource;

  @Override
  public int getMaxHealth() {
    return maxHealth;
  }

  @Override
  public int getCurrentHealth() {
    return currentHealth;
  }

  @Override
  public void removeHealth(final int amount) {
    currentHealth -= amount;
    if (currentHealth <= 0) {
      onDeath();
    }
  }

  @Override
  public void addHealth(final int amount) {
    currentHealth += amount;
    if (currentHealth < maxHealth) {
      currentHealth = maxHealth;
    }
  }

  @Override
  public void onDeath() {
    emptySchematic.pasteToGround(protectedRegion.getGroundCenter(), false);
  }

  @Override
  public void onBlockBreak(final BlockBreakEvent event) {
    event.setCancelled(true);
    final Player player = event.getPlayer();
    final boolean hasLevel = jobDataManager.getHolder(player.getUniqueId()).getJobData(jobType).hasLevel(jobLevel);

    if (!hasLevel) {
      final String jobName = Message.elem(jobType.getDisplayName());
      final String lvl = Message.elem("" + jobLevel);
      final String rec = Message.elem("" + resource.getDisplayName() + "e");
      Message.error(player, JobModule.class, "Du benötigst Level " + lvl + " in " + jobName + ", um " + rec + " abzubauen.");
    }

  }

  @Override
  public void onInteract(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();

    final boolean hasLevel = jobDataManager.getHolder(player.getUniqueId()).getJobData(jobType).hasLevel(jobLevel);

    Message.send(player, JobModule.class, "§6Resource:");
    Message.send(player, JobModule.class, "§f- §9Typ §f" + resource.getDisplayName());
    Message.send(player, JobModule.class, "§f- §9Job §f" + jobType.getDisplayName());
    Message.send(player, JobModule.class, "§f- §9Level " + (hasLevel ? "§a" : "§c") + jobLevel);
  }

}
