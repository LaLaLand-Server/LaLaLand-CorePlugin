package de.lalaland.core.modules.jobs.jobdata;

import com.google.gson.JsonObject;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.ui.Message;
import de.lalaland.core.utils.common.UtilPlayer;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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

  public JobData(final JobType jobType, final JobDataManager manager, final UUID userID) {
    this.userID = userID;
    this.jobType = jobType;
    jobDataManager = manager;
    exp = 0L;
    level = 1;
    lastLevelExp = manager.getMinExpOfLevel(level - 1);
    nextLevelExp = manager.getMinExpOfLevel(level + 1);
    maxExp = manager.getMinExpOfLevel(JobDataManager.LEVEL_HARD_CAP);
  }

  public JobData(final JsonObject json, final JobDataManager manager, final UUID userID) {
    this.userID = userID;
    jobDataManager = manager;
    exp = json.get("Exp").getAsLong();
    jobType = JobType.valueOf(json.get("JobType").getAsString());
    level = manager.getMaxLevelOfExp(exp);
    lastLevelExp = manager.getMinExpOfLevel(level - 1);
    nextLevelExp = manager.getMinExpOfLevel(level + 1);
    maxExp = manager.getMinExpOfLevel(JobDataManager.LEVEL_HARD_CAP);
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
  private final UUID userID;

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
    final Player player = Bukkit.getPlayer(userID);
    final String  expAdd = Message.elem("" + value + " Exp");
    final String  jobName = Message.elem("" + jobType.getDisplayName());
    Message.send(player, JobModule.class, "Du erhälst " + expAdd + " in " + jobName + ".");
    UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 1.35F);
    if (exp >= nextLevelExp) {
      incrementLevel(false);
    }
  }

  private void incrementLevel(final boolean updateExp) {
    level++;
    lastLevelExp = jobDataManager.getMinExpOfLevel(level);
    nextLevelExp = jobDataManager.getMinExpOfLevel(level + 1);
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
