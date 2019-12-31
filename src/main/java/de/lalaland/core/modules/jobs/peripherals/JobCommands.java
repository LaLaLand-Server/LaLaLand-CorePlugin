package de.lalaland.core.modules.jobs.peripherals;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.jobs.guis.JobGUI;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.jobs.jobdata.JobHolder;
import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.utils.Message;
import de.lalaland.core.utils.common.UtilPlayer;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 30.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
@CommandAlias("jobs")
public class JobCommands extends BaseCommand {

  private final JobDataManager jobDataManager;

  @Default
  public void onDefault(final Player sender) {
    JobGUI.create(jobDataManager.getHolder(sender.getUniqueId())).open(sender);
    UtilPlayer.playSound(sender, Sound.UI_BUTTON_CLICK, 0.8F, 1.2F);
  }

  @Subcommand("show")
  public void onShow(final Player sender) {
    final JobHolder holder = jobDataManager.getHolder(sender.getUniqueId());
    for (final JobType type : JobType.values()) {
      final String lvl = Message.elem("" + holder.getJobData(type).getLevel());
      Message.send(sender, JobModule.class, type.getDisplayName() + ": " + lvl);
    }
  }

}
