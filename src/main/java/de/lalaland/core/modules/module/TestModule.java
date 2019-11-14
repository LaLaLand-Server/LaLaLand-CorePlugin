package de.lalaland.core.modules.module;

import de.lalaland.core.modules.IModule;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 14.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class TestModule implements IModule {

  @Override
  public void enable() throws Exception {

  }

  @Override
  public void disable() throws Exception {

  }

  @Override
  public String getModuleName() {
    return "TestModule";
  }
}
