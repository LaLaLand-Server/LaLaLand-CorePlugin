package de.lalaland.core.modules.jobs;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.jobs.jobdata.JobDataListener;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.jobs.peripherals.JobCommands;
import lombok.Getter;
import org.bukkit.Bukkit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobModule implements IModule {

  @Getter
  private JobDataManager jobDataManager;

  @Override
  public String getModuleName() {
    return "JobModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    jobDataManager = new JobDataManager(plugin);
    Bukkit.getPluginManager().registerEvents(new JobDataListener(jobDataManager, plugin.getTaskManager()), plugin);
    plugin.getCommandManager().registerCommand(new JobCommands(jobDataManager));
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
