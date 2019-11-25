package de.lalaland.core.modules.structures.core;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.modules.structures.StructureModule;
import de.lalaland.core.ui.Message;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("struct")
@CommandPermission("admin")
public class StructureCommand extends BaseCommand {

  public StructureCommand(final SchematicManager schematicManager, final StructureManager structureManager) {
    this.schematicManager = schematicManager;
    this.structureManager = structureManager;
  }

  private final SchematicManager schematicManager;
  private final StructureManager structureManager;

  @Default
  public void onDefault(final Player sender) {
    Message.send(sender, StructureModule.class, "Es gibt nur weitere Subcommands.");
  }

  @Subcommand("create fromschematic")
  @CommandCompletion("@Structure")
  public void onCreate(final Player sender, final String schematicString) {
    final Schematic schematic = schematicManager.getSchematic(schematicString);
    if (schematic == null) {
      Message.error(sender, StructureModule.class, "Dieses Schematic existiert nicht.");
      return;
    }
    structureManager.createBaseStructure(schematic, sender.getLocation());
  }

}
