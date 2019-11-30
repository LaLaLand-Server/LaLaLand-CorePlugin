package de.lalaland.core.modules.jobs.jobdata;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.gson.JsonObject;
import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
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
    expToLevelMap = TreeRangeMap.create();
    levelToExpMap = new Int2LongOpenHashMap();
    generateExpMaps();
  }

  private final Object2ObjectMap<UUID, JobHolder> jobHolderMap;
  private final File jobDataFolder;
  private final CorePlugin plugin;
  private final RangeMap<Long, Integer> expToLevelMap;
  private final Int2LongMap levelToExpMap;

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
      final JobHolder holder = new JobHolder(plugin.getGson().fromJson(builder.toString(), JsonObject.class), this, userID);
      jobHolderMap.put(userID, holder);
    } else {
      jobHolderMap.put(userID, new JobHolder(this, userID));
    }
  }

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

}
