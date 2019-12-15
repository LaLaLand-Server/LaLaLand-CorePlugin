package de.lalaland.core.modules.economy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.economy.dropapi.MoneyDropManager;
import de.lalaland.core.ui.Message;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
@CommandAlias("money")
public class EconomyCommand extends BaseCommand {

  private final MoneyDropManager moneyDropManager;

  @Subcommand("createdrop")
  public void onDrop(final Player sender, final int amount) {
    final RayTraceResult trace = sender.rayTraceBlocks(16D);
    if (trace == null) {
      Message.error(sender, EconomyModule.class, "Bitte auf einen Block schauen.");
      return;
    }
    final Block block = trace.getHitBlock();
    if (block == null) {
      Message.error(sender, EconomyModule.class, "Bitte auf einen Block schauen.");
      return;
    }
    final Location dropLoc = block.getRelative(BlockFace.UP).getLocation().clone().add(0.5, 1.0, 0.5);
    moneyDropManager.dropMoney(dropLoc, amount);
    Message.send(sender, EconomyModule.class, "Es wurden §e" + amount + " Gold §7gedroppt.");
  }

}
