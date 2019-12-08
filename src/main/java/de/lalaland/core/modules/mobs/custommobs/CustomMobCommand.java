package de.lalaland.core.modules.mobs.custommobs;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
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
  public void spawnMob(final Player issuer, final ComplexModelType type) {
    customMobManager.spawnModeled(type, issuer.getLocation());
  }

  @Subcommand("disguise")
  public void disguise(final Player issuer, final String name) {
    final PlayerDisguise dis = new PlayerDisguise(name);
    dis.setViewSelfDisguise(true);
    dis.setEntity(issuer);
    dis.startDisguise();
  }

}
