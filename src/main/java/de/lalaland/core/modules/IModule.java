package de.lalaland.core.modules;

import de.lalaland.core.CorePlugin;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public interface IModule {

  String getModuleName();

  void enable(CorePlugin plugin) throws Exception;

  void disable(CorePlugin plugin) throws Exception;

}
