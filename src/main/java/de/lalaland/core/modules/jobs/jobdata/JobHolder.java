package de.lalaland.core.modules.jobs.jobdata;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.EnumMap;
import java.util.Map.Entry;
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
public class JobHolder {

  public JobHolder(final JobDataManager jobDataManager, final UUID playerID) {
    this.playerID = playerID;
    this.jobDataManager = jobDataManager;
    jobDataMap = Maps.newEnumMap(JobType.class);
    for (final JobType job : JobType.values()) {
      jobDataMap.put(job, new JobData(job, jobDataManager, playerID));
    }
  }

  public JobHolder(final JsonObject json, final JobDataManager jobDataManager, final UUID playerID) {
    this.playerID = playerID;
    this.jobDataManager = jobDataManager;
    jobDataMap = Maps.newEnumMap(JobType.class);
    for (final JobType job : JobType.values()) {
      jobDataMap.put(job, new JobData(job, jobDataManager, playerID));
    }
    for (final Entry<String, JsonElement> entry : json.entrySet()) {
      final JobType job = JobType.valueOf(entry.getKey());
      final JobData data = new JobData(entry.getValue().getAsJsonObject(), jobDataManager, playerID);
      jobDataMap.put(job, data);
    }
  }

  private final UUID playerID;
  private final JobDataManager jobDataManager;
  private final EnumMap<JobType, JobData> jobDataMap;

  public JobData getJobData(final JobType type) {
    return jobDataMap.get(type);
  }

  public JsonObject getAsJson() {
    final JsonObject json = new JsonObject();
    for (final JobType jobType : JobType.values()) {
      json.add(jobType.toString(), jobDataMap.get(jobType).getAsJson());
    }
    return json;
  }

}
