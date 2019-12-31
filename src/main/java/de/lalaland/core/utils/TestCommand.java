package de.lalaland.core.utils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.comphenix.protocol.PacketType.Play;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.tasks.TaskManager;
import de.lalaland.core.tasks.TeleportParticleThread;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.holograms.MovingHologram;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

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

  public TestCommand(final HologramManager holoManager) {
    this.holoManager = holoManager;
  }

  private final HologramManager holoManager;

  @Subcommand("holo")
  public void onHolo(final Player sender, final String line) {
    final MovingHologram hologram = holoManager.createMovingHologram(sender.getLocation(), new Vector(0, 0.1, 0), 60);
    hologram.getHologram().appendTextLine("§eTest");
  }

  @Subcommand("waiting")
  public void onWait(final Player sender, final int ticks) {
    UtilPlayer.forceWait(sender, ticks, true, pl -> pl.sendMessage("Wait is over."), pl -> pl.sendMessage("Wait cancel."));
  }


  @Subcommand("teleport")
  public void testTeleport(Player sender) {
    TeleportParticleThread warmup = new TeleportParticleThread(sender);
    UtilPlayer.forceWait(sender, 200, true, p -> {
      sender.sendMessage("Erfolgreich");
      warmup.cancel();
    }, cancel -> {
      sender.sendMessage("Abgebrochen");
      warmup.cancel();
    });
  }
}