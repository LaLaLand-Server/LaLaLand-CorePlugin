package de.lalaland.core.modules.schematics.editor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.schematics.SchematicModule;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.modules.schematics.core.SimpleSchematic;
import de.lalaland.core.ui.Message;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 23.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("schematic")
public class SchematicCommand extends BaseCommand {

  public SchematicCommand(final SchematicManager schematicManager) {
    this.schematicManager = schematicManager;
  }

  private final SchematicManager schematicManager;

  @Default
  public void onDefault(final Player sender) {
    Message.send(sender, SchematicModule.class, "Alle geladenen Schematics:");
    for (final SimpleSchematic schematic : schematicManager) {
      sender.sendMessage("§f - §7" + schematic.getSchmaticID());
    }
  }

  @Subcommand("test1")
  public void onTest(final Player sender, final int size, final String name) {
    final Block middle = sender.getLocation().getBlock();
    final Block bot = middle.getRelative(-size, -size, -size);
    final Block top = middle.getRelative(size, size, size);
    schematicManager.createSimple(bot, top, name);
    Message.send(sender, SchematicModule.class, "Schematic wurde erstellt.");
  }

  @Subcommand("paste corner")
  public void onPaste(final Player sender, final String schematicName) {
    final SimpleSchematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.paste(sender.getLocation());
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

  @Subcommand("paste middle")
  public void onPasteMiddle(final Player sender, final String schematicName) {
    final SimpleSchematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.pasteCenteredAround(sender.getLocation());
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

  @Subcommand("paste ground")
  public void onOnGround(final Player sender, final String schematicName) {
    final SimpleSchematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.pasteToGround(sender.getLocation());
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

}
