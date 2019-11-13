package de.lalaland.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lalaland.core.user.UserManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class CorePlugin extends JavaPlugin {

  @Getter
  private Gson gson;
  @Getter
  private UserManager userManager;

  @Override
  public void onLoad() {

  }

  @Override
  public void onEnable() {
    init();
  }

  @Override
  public void onDisable() {

  }

  private void init() {
    gson = new GsonBuilder().setPrettyPrinting().create();
    userManager = new UserManager(this);
  }

}
