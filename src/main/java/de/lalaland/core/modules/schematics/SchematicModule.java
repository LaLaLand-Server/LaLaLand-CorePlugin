package de.lalaland.core.modules.schematics;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.schematics.core.SchematicManager;
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
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {
    schematicManager.saveSchematics();
  }

}
