package de.lalaland.core.modules.structures.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.protection.ProtectionModule;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.schematics.SchematicModule;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.utils.UtilModule;
import de.lalaland.core.utils.actionbar.ActionBarManager;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class StructureManager {

  public StructureManager(final CorePlugin plugin) {
    structureRegionMap = new Object2ObjectOpenHashMap<>();
    regionManager = plugin.getModule(ProtectionModule.class).getRegionManager();
    hologramManager = plugin.getModule(UtilModule.class).getHologramManager();
    actionBarManager = plugin.getModule(UtilModule.class).getActionBarManager();
    jobDataManager = plugin.getModule(JobModule.class).getJobDataManager();
    schematicManager = plugin.getModule(SchematicModule.class).getSchematicManager();
    userManager = plugin.getUserManager();
    resourceRespawnThread = new ResourceRespawnThread();
    structureFolder = new File(plugin.getDataFolder() + File.separator + "structures");
    structureFolder.mkdirs();
    plugin.getTaskManager().runRepeatedBukkit(resourceRespawnThread, 0L, 5L);
  }

  @Getter(AccessLevel.PROTECTED)
  private final ResourceRespawnThread resourceRespawnThread;
  @Getter(AccessLevel.PROTECTED)
  private final HologramManager hologramManager;
  @Getter(AccessLevel.PROTECTED)
  private final ActionBarManager actionBarManager;
  @Getter(AccessLevel.PROTECTED)
  private final JobDataManager jobDataManager;
  @Getter(AccessLevel.PROTECTED)
  private final UserManager userManager;
  @Getter(AccessLevel.PROTECTED)
  private final SchematicManager schematicManager;
  private final RegionManager regionManager;
  private final Object2ObjectOpenHashMap<ProtectedRegion, Structure> structureRegionMap;
  private final File structureFolder;

  public Structure getStructure(final ProtectedRegion region) {
    return structureRegionMap.get(region);
  }

  public void deleteStructure(final Structure structure) {
    structure.destroyBlocks();
    unlinkStructure(structure);
  }

  protected void unlinkStructure(final Structure struct) {
    final ProtectedRegion region = struct.getProtectedRegion();
    structureRegionMap.remove(region);
    regionManager.removeRegion(region);
  }

  public BaseStructure createBaseStructure(final Schematic baseSchematic, final Location groundCenter) {
    final ProtectedRegion schematicRegion = regionManager.createFor(baseSchematic, groundCenter);
    final BaseStructure structure = new BaseStructure(schematicRegion, baseSchematic, this);
    structureRegionMap.put(schematicRegion, structure);
    return structure;
  }

  public JobResource createResource(final ResourceType resourceType, final Location groundLocation) {
    final int schematicIndex = resourceType.getRandomSchematicIndex();
    final Schematic baseSchematic = schematicManager.getSchematic(resourceType.toString() + "_BASE_" + schematicIndex);
    final ProtectedRegion region = regionManager.createFor(baseSchematic, groundLocation);
    final JobResource resource = new JobResource(region, this, schematicIndex, resourceType);
    resource.buildBlocks();
    structureRegionMap.put(region, resource);
    return resource;
  }

  public void saveStructureStages(final CorePlugin plugin) throws IOException {
    final Gson gson = plugin.getGson();
    for (final Structure struct : structureRegionMap.values()) {
      final JsonObject json = struct.asJsonObject();
      final String folderName = json.get("FolderName").getAsString();
      final File folder = new File(structureFolder + File.separator + folderName);
      folder.mkdir();
      final File structFile = new File(folder, struct.getProtectedRegion().getRegionID().toString() + ".json");
      final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(structFile));
      osw.write(gson.toJson(struct.asJsonObject()));
      osw.close();
    }
  }

  public void loadStructureStages(final CorePlugin plugin) throws IOException {
    final Gson gson = plugin.getGson();
    for (final File subFolder : structureFolder.listFiles()) {
      for (final File structFile : subFolder.listFiles()) {
        final InputStreamReader isr = new InputStreamReader(new FileInputStream(structFile));
        final StringBuilder builder = new StringBuilder();
        int read;
        while ((read = isr.read()) != -1) {
          builder.append((char) read);
        }
        final JsonObject json = gson.fromJson(builder.toString(), JsonObject.class);
        if (json.get("FolderName").getAsString().equals("resources")) {
          deserializeResource(json);
        }
      }
    }
  }

  private void deserializeResource(final JsonObject json) {
    final ProtectedRegion region = regionManager.getRegionByID(UUID.fromString(json.get("RegionID").getAsString()));
    final int schematicIndex = json.get("SchematicIndex").getAsInt();
    final ResourceType resourceType = ResourceType.valueOf(json.get("ResourceType").getAsString());
    final JobResource resource = new JobResource(region, this, schematicIndex, resourceType);
    final long scheduledRespawn = json.get("NextRespawn").getAsLong();
    resource.setScheduledRespawn(scheduledRespawn);
    resource.setDepleted(json.get("Depleted").getAsBoolean());
    structureRegionMap.put(region, resource);
    if (resource.isDepleted()) {
      resourceRespawnThread.addResourceToRespawn(resource);
    }
  }

}