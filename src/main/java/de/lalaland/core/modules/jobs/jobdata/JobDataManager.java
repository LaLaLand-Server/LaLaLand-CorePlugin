package de.lalaland.core.modules.jobs.jobdata;

import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobDataManager {

  protected static final int LEVEL_HARD_CAP = 50;

  public JobDataManager(final CorePlugin plugin) {
    this.plugin = plugin;
    jobHolderMap = new Object2ObjectOpenHashMap<>();
    jobDataFolder = new File(plugin.getDataFolder() + File.separator + "jobdata");
    jobDataFolder.mkdirs();
  }

  private final Object2ObjectMap<UUID, JobHolder> jobHolderMap;
  private final File jobDataFolder;
  private final CorePlugin plugin;

  public JobHolder getHolder(final UUID userID) {
    return jobHolderMap.get(userID);
  }

  public void addExp(final UUID userID, final JobType job, final long amount) {
    jobHolderMap.get(userID).getJobData(job).addExp(amount);
  }

  protected void saveUser(final UUID userID) throws IOException {
    final File userFile = new File(jobDataFolder, userID.toString() + ".json");
    final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(userFile));
    osw.write(plugin.getGson().toJson(jobHolderMap.get(userID).getAsJson()));
    osw.close();
  }

  protected void loadUser(final UUID userID) throws IOException {
    final File userFile = new File(jobDataFolder, userID.toString() + ".json");
    if (userFile.exists()) {
      final InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(userFile));
      int read;
      final StringBuilder builder = new StringBuilder();
      while ((read = inputStreamReader.read()) != -1) {
        builder.append((char) read);
      }
      final JobHolder holder = new JobHolder(plugin.getGson().fromJson(builder.toString(), JsonObject.class), this);
      jobHolderMap.put(userID, holder);
    } else {
      jobHolderMap.put(userID, new JobHolder(this));
    }
  }

  protected int getLevelOfExp(final long exp) {
    return 0;
  }

  protected long getExpForLevel(final int level) {
    return 0L;
  }

}
