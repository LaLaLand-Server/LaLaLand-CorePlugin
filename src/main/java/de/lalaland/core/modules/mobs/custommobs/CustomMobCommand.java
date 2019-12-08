package de.lalaland.core.modules.mobs.custommobs;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
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

@CommandAlias("custommob")
public class CustomMobCommand extends BaseCommand {

  public CustomMobCommand(final CustomMobManager customMobManager) {
    this.customMobManager = customMobManager;
  }

  private final CustomMobManager customMobManager;

  @Subcommand("spawn")
  public void spawnMob(final Player issuer, final BiPipedType type) {
    customMobManager.spawnBiPiped(type, issuer.getLocation());
  }

}
