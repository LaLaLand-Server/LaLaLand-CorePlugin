package de.lalaland.core.modules.jobs.jobdata;

import com.google.gson.JsonObject;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobData {

  public JobData(final JobType jobType, final JobDataManager manager) {
    this.jobType = jobType;
    jobDataManager = manager;
    exp = 0L;
    level = 1;
    lastLevelExp = manager.getExpForLevel(level - 1);
    nextLevelExp = manager.getExpForLevel(level + 1);
    maxExp = manager.getExpForLevel(JobDataManager.LEVEL_HARD_CAP);
  }

  public JobData(final JsonObject json, final JobDataManager manager) {
    jobDataManager = manager;
    exp = json.get("Exp").getAsLong();
    jobType = JobType.valueOf(json.get("JobType").getAsString());
    level = manager.getLevelOfExp(exp);
    lastLevelExp = manager.getExpForLevel(level - 1);
    nextLevelExp = manager.getExpForLevel(level + 1);
    maxExp = manager.getExpForLevel(JobDataManager.LEVEL_HARD_CAP);
  }

  private final long maxExp;
  private final JobDataManager jobDataManager;
  @Getter
  private final JobType jobType;
  @Getter
  private long exp;
  @Getter
  private int level;
  @Getter
  private long nextLevelExp;
  @Getter
  private long lastLevelExp;

  public JsonObject getAsJson() {
    final JsonObject json = new JsonObject();
    json.addProperty("Exp", exp);
    json.addProperty("JobType", jobType.toString());
    return json;
  }

  public double getLevelProgress() {
    return (1.0D / getExpDelta()) * getExpFromLastLevel();
  }

  public long getExpDelta() {
    return nextLevelExp - lastLevelExp;
  }

  public long getExpFromLastLevel() {
    return exp - lastLevelExp;
  }

  public long getExpToNextLevel() {
    return nextLevelExp - exp;
  }

  public void addLevel(int amount) {
    while (amount-- > 0) {
      incrementLevel(true);
    }
  }

  public boolean hasLevel(final int lvl) {
    return level >= lvl;
  }

  public void addExp(final long value) {
    if (exp + value >= maxExp) {
      exp = maxExp;
      if (level != JobDataManager.LEVEL_HARD_CAP) {
        incrementLevel(false);
      }
      return;
    }

    exp += value;
    if (exp >= nextLevelExp) {
      incrementLevel(false);
    }
  }

  private void incrementLevel(final boolean updateExp) {
    level++;
    lastLevelExp = jobDataManager.getLevelOfExp(level);
    nextLevelExp = jobDataManager.getExpForLevel(level + 1);
    if (updateExp) {
      exp = lastLevelExp + 1;
    }
  }

  public void removeExp(final long value) {
    exp -= value;
    if (exp <= 0) {
      exp = 0;
    }
  }

}
