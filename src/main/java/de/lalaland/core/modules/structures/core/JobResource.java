package de.lalaland.core.modules.structures.core;

import com.google.gson.JsonObject;
import de.lalaland.core.modules.combat.items.StatItem;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.jobs.jobdata.JobHolder;
import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.utils.Message;
import de.lalaland.core.utils.actionbar.ActionBarBoard;
import de.lalaland.core.utils.actionbar.ActionBarBoard.Section;
import de.lalaland.core.utils.actionbar.ActionBarManager;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.holograms.MovingHologram;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 27.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobResource extends Structure implements Healthed, Comparable<JobResource> {

  private static final Vector BLOCK_OFFSET = new Vector(0.5, 0.5, 0.5);
  private static final Vector HOLO_VELOCITY = new Vector(0, 0.075, 0);

  protected JobResource(final ProtectedRegion protectedRegion,
      final StructureManager structureManager,
      final int schematicIndex,
      final ResourceType resource) {
    super(protectedRegion, structureManager.getSchematicManager().getSchematic(resource.toString() + "_BASE_" + schematicIndex),
        structureManager);
    this.schematicIndex = schematicIndex;
    jobType = resource.getJobType();
    jobLevel = resource.getJobLevel();
    maxHealth = resource.getMaxHealth();
    currentHealth = maxHealth;
    emptySchematic = structureManager.getSchematicManager().getSchematic(resource.toString() + "_EMPTY_" + schematicIndex);
    respawnTimeDelta = resource.getRespawnTimeMS();
    jobDataManager = structureManager.getJobDataManager();
    this.resource = resource;
    userManager = structureManager.getUserManager();
    hologramManager = structureManager.getHologramManager();
    actionBarManager = structureManager.getActionBarManager();
    resourceRespawnThread = structureManager.getResourceRespawnThread();
    for (final Material mat : resource.getValidMaterials()) {
      addValidMaterial(mat);
    }
  }

  private final int schematicIndex;
  private final JobDataManager jobDataManager;
  private final JobType jobType;
  private final int jobLevel;
  private final int maxHealth;
  private final Schematic emptySchematic;
  private int currentHealth;
  private final long respawnTimeDelta;
  private final ResourceType resource;
  private final UserManager userManager;
  private final HologramManager hologramManager;
  private final ActionBarManager actionBarManager;
  private final ResourceRespawnThread resourceRespawnThread;
  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  private long scheduledRespawn = System.currentTimeMillis();
  @Getter
  @Setter(AccessLevel.PROTECTED)
  boolean depleted = false;

  @Override
  public int getMaxHealth() {
    return maxHealth;
  }

  @Override
  public int getCurrentHealth() {
    return currentHealth;
  }

  public void heal() {
    currentHealth = maxHealth;
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

  protected void buildEmpty() {
    emptySchematic.pasteToGround(protectedRegion.getGroundCenter(), false);
  }

  protected long timeToNextRespawn() {
    return scheduledRespawn - System.currentTimeMillis();
  }

  @Override
  public void onDeath() {
    buildEmpty();
    depleted = true;
    scheduledRespawn = System.currentTimeMillis() + respawnTimeDelta;
    resourceRespawnThread.addResourceToRespawn(this);
  }

  @Override
  public void onBlockBreak(final BlockBreakEvent event) {
    event.setCancelled(true);
    if (!isValidMaterial(event.getBlock().getType())) {
      return;
    }
    final Player player = event.getPlayer();
    final ItemStack item = player.getInventory().getItemInMainHand();
    final StatItem statItem = StatItem.of(item);
    final double jobDmg = statItem.getJobValue(jobType);
    final JobHolder jobHolder = jobDataManager.getHolder(player.getUniqueId());

    final boolean hasLevel = jobHolder.getJobData(jobType).hasLevel(jobLevel);
    final boolean hasTool = statItem.getJobRequirement(jobType) >= jobLevel;
    final boolean canUseTool = statItem.canUseForJob(jobDataManager, userManager.getUser(player.getUniqueId()));

    if (depleted) {
      Message.error(player, JobModule.class, "Diese Resource ist momentan erschöpft.");
      return;
    }

    if (!hasLevel) {
      final String jobName = Message.elem(jobType.getDisplayName());
      final String lvl = Message.elem("" + jobLevel);
      final String rec = Message.elem("" + resource.getDisplayName() + "e");
      Message.error(player, JobModule.class, "Du benötigst Level " + lvl + " in " + jobName + ", um " + rec + " abzubauen.");
      return;
    }

    if (!hasTool) {
      final String jobName = Message.elem(jobType.getDisplayName());
      final String lvl = Message.elem("" + jobLevel);
      final String rec = Message.elem("" + resource.getDisplayName() + "e");
      Message.error(player, JobModule.class,
          "Du benötigst ein Werkzeug mit dem Level " + lvl + " in " + jobName + ", um " + rec + " abzubauen.");
      return;
    }

    if (!canUseTool) {
      Message.error(player, JobModule.class, "Du kannst dieses Werkzeug nicht verwenden.");
      return;
    }

    final int dmg = (int) (jobDmg + 0.5);
    removeHealth(dmg);

    // TODO Actionbar add custom HP bar
    final ActionBarBoard board = actionBarManager.getBoard(player);
    board.getSection(Section.MIDDLE).addTokenLayer(20, "RESOURCE_HP", 100, () -> {
      final String bar = UtilMath.getPercentageBar(currentHealth, maxHealth, 20, "∎");
      final String element = bar + " §f" + currentHealth + "/" + maxHealth + " ";
      return element;
    });
    actionBarManager.updateAndShow(player);

    final Vector playerToBlockVec = event.getBlock()
        .getLocation()
        .toVector()
        .add(BLOCK_OFFSET)
        .subtract(player.getEyeLocation().toVector());
    playerToBlockVec.multiply(0.75);
    final Vector posVec = player.getEyeLocation().toVector().add(playerToBlockVec);
    final Location holoLoc = new Location(player.getWorld(), posVec.getX(), posVec.getY(), posVec.getZ());
    final MovingHologram mov = hologramManager.createMovingHologram(holoLoc, HOLO_VELOCITY, 20);
    mov.getHologram().appendTextLine("§e-" + dmg);

    if (isDepleted()) {
      // TODO play break sound / add break sound to resource enum
      jobHolder.getJobData(jobType).addExp(resource.getJobExp());
      resource.drop(player.getInventory(), player.getLocation());
    }
  }

  @Override
  public void onInteract(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }
    final boolean hasLevel = jobDataManager.getHolder(player.getUniqueId()).getJobData(jobType).hasLevel(jobLevel);

    Message.send(player, JobModule.class, "§6Resource:");
    Message.send(player, JobModule.class, "§f- §9Typ §f" + resource.getDisplayName());
    Message.send(player, JobModule.class, "§f- §9Job §f" + jobType.getDisplayName());
    Message.send(player, JobModule.class, "§f- §9Level " + (hasLevel ? "§a" : "§c") + jobLevel);
  }

  @Override
  public int compareTo(@NotNull final JobResource other) {
    return (int) (respawnTimeDelta - other.respawnTimeDelta);
  }

  @Override
  public JsonObject asJsonObject() {
    final JsonObject json = new JsonObject();
    json.addProperty("FolderName", "resources");
    json.addProperty("RegionID", protectedRegion.getRegionID().toString());
    json.addProperty("SchematicIndex", schematicIndex);
    json.addProperty("ResourceType", resource.toString());
    json.addProperty("NextRespawn", scheduledRespawn);
    json.addProperty("Depleted", depleted);
    return json;
  }

}
