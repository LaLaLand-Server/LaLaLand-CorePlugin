package de.lalaland.core.modules.combat.items;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.ui.Message;
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
  public void onCreateWeapon(final Player sender, final BaseWeapon weapon) {
    sender.getInventory().addItem(weapon.createBaseItem());
    Message.send(sender, CombatModule.class,
        "Du erhälst ein Item vom Typ " + Message.elem(weapon.toString()));
  }

  @Subcommand("create tool")
  public void onCreateArmor(final Player sender, final BaseTool tool) {
    sender.getInventory().addItem(tool.createBaseItem());
    Message.send(sender, CombatModule.class,
        "Du erhälst ein Item vom Typ " + Message.elem(tool.toString()));
  }

  @Subcommand("create armor")
  public void onCreateTool(final Player sender, final BaseArmor armor) {
    Message.error(sender, CombatModule.class, "Noch nicht implementiert.");
  }


}
