package de.lalaland.core.modules.resourcepack.skins;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.resourcepack.ResourcepackModule;
import de.lalaland.core.ui.Message;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("modelitem")
@CommandPermission("admin")
public class ModelItemCommand extends BaseCommand {

  @Default
  public void onCommand(final Player sender) {
    Message.send(sender, ResourcepackModule.class, "Benutze '/modelitem get <ModelItem>' um ein ModelItem zu erhalten.");
  }

  @Subcommand("get")
  @CommandCompletion("@ModelItem")
  public void onGetCommand(final Player sender, final ModelItem model) {
    sender.getInventory().addItem(model.create());
    final String modelName = Message.elem(model.toString());
    Message.send(sender, ResourcepackModule.class, "Du hast ein ModelItem erhalten: " + modelName);
  }

  @Subcommand("tell")
  @CommandCompletion("@ModelItem")
  public void onTellCommand(final Player sender, final ModelItem model) {
    Message.send(sender, ResourcepackModule.class, "Model: " + model.getChar());
  }

}
