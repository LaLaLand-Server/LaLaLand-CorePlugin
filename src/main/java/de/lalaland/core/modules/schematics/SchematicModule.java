package de.lalaland.core.modules.schematics;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.schematics.core.SchematicManager;
import de.lalaland.core.modules.schematics.editor.EditorSessions;
import de.lalaland.core.modules.schematics.editor.SchematicCommand;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 20.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SchematicModule implements IModule {

  @Getter
  private SchematicManager schematicManager;

  @Override
  public String getModuleName() {
    return "SchematicModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    schematicManager = new SchematicManager(plugin);
    schematicManager.loadSchematics();
    final EditorSessions sessions = new EditorSessions(plugin);
    plugin.getCommandManager().registerCommand(new SchematicCommand(schematicManager, sessions));
    // TODO register autocompletion
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {
    schematicManager.saveSchematics();
  }

}
