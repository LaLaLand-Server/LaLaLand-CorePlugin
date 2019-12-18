package de.lalaland.core.modules.mobs.implementations;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.mobs.MobModule;
import de.lalaland.core.utils.Message;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("mobs")
public class GameMobCommand extends BaseCommand {

  public GameMobCommand(final MobManager mobManager) {
    this.mobManager = mobManager;
  }

  private final MobManager mobManager;

  @Subcommand("spawn")
  public void spawn(final Player player, final GameMobType type, final int level) {
    mobManager.createMob(type, level, player.getLocation());
    Message.send(player, MobModule.class, "Mob wurde gespawnt.");
  }

}
