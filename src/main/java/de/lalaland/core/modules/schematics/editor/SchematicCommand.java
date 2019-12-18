package de.lalaland.core.modules.schematics.editor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.schematics.SchematicModule;
import de.lalaland.core.modules.schematics.core.Schematic;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.utils.Message;
import de.lalaland.core.utils.tuples.Pair;
import org.bukkit.Location;
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

  private final EditorSessions sessions;

  public SchematicCommand(final SchematicManager schematicManager, final EditorSessions sessions) {
    this.schematicManager = schematicManager;
    this.sessions = sessions;
  }

  private final SchematicManager schematicManager;

  @Default
  public void onDefault(final Player sender) {
    Message.send(sender, SchematicModule.class, "Alle geladenen Schematics:");
    for (final Schematic schematic : schematicManager) {
      sender.sendMessage("§f - §7" + schematic.getSchmaticID());
    }
  }

  @Subcommand("paste corner")
  @CommandCompletion("@Structure")
  public void onPaste(final Player sender, final String schematicName) {
    final Schematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.paste(sender.getLocation(), false);
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

  @Subcommand("paste middle")
  @CommandCompletion("@Structure")
  public void onPasteMiddle(final Player sender, final String schematicName) {
    final Schematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.pasteCenteredAround(sender.getLocation(), false);
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

  @Subcommand("paste ground")
  @CommandCompletion("@Structure")
  public void onOnGround(final Player sender, final String schematicName) {
    final Schematic schematic = schematicManager.getSchematic(schematicName);
    if (schematic == null) {
      Message.error(sender, SchematicModule.class, "Schematic existiert nicht.");
      return;
    }
    schematic.pasteToGround(sender.getLocation(), false);
    Message.send(sender, SchematicModule.class, "Schematic wird geladen.");
  }

  @Subcommand("editor")
  public void addToEditor(final Player sender) {
    if (sessions.hasSession(sender)) {
      sessions.removeSession(sender);
      sender.sendMessage("Editor wurde beendet");
      return;
    }
    sessions.addSession(sender);
    sender.sendMessage("Wähle Position 1 & 2 -> /schematic create");
  }

  @Subcommand("create")
  public void createSchematic(final Player sender, final String schematicName) {

    final Pair<Location, Location> locs = sessions.getSelectedSession(sender);

    if (locs.getLeft() == null || locs.getRight() == null) {
      sender.sendMessage("Du musst erst einen Bereich auswählen.");
      return;
    }

    final Schematic schematic = schematicManager.create(locs.getLeft().getBlock(), locs.getRight().getBlock(), schematicName);

    sender.sendMessage("Schematic wurde erstellt.");

  }

}
