package de.lalaland.core.utils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.utils.holograms.AbstractHologram;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 17.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("test")
public class TestCommand extends BaseCommand {

  public TestCommand(HologramManager holoManager){
    this.holoManager = holoManager;
  }

  private final HologramManager holoManager;

  @Subcommand("holo")
  public void onHolo(Player sender, String line) {
    AbstractHologram hologram = this.holoManager.createHologram(sender.getLocation());
    hologram.appendTextLine(line);
  }

}
