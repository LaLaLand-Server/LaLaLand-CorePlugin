package de.lalaland.core.modules.loot;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.loot.tables.LootTableManager;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 17.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LootModule implements IModule {

  @Getter
  private LootTableManager lootTableManager;

  @Override
  public String getModuleName() {
    return "LootModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception, Exception {
    lootTableManager = new LootTableManager();
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
