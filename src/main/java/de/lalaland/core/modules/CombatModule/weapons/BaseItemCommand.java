package de.lalaland.core.modules.CombatModule.weapons;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandPermission("admin")
@CommandAlias("baseitem")
public class BaseItemCommand extends BaseCommand {

  @Subcommand("create weapon")
  public void onCreateCommand(Player sender, BaseWeapon weapon) {
    sender.getInventory().addItem(weapon.createBaseItem());
  }


}
