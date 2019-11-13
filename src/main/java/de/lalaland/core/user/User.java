package de.lalaland.core.user;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.data.UserData;
import de.lalaland.core.utils.io.GsonFileReader;
import de.lalaland.core.utils.io.GsonFileWriter;
import de.lalaland.core.utils.io.IReader;
import de.lalaland.core.utils.io.IWriter;
import de.lalaland.core.utils.io.mongodb.MongoDataReader;
import de.lalaland.core.utils.io.mongodb.MongoDataWriter;
import de.lalaland.core.utils.tuples.Unit;
import java.io.File;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class User {

  public static boolean SAVE_DATA_IN_DATABASE = false;

  private final CorePlugin corePlugin;
  @Getter
  private final UUID uuid;
  @Getter
  private final UserData userData;
  @Getter
  private final File userDataDirectory;
  @Getter
  private final Unit<Player> onlinePlayer;

  public User(final CorePlugin corePlugin, final UUID uuid) {
    this.corePlugin = corePlugin;
    this.uuid = uuid;
    userDataDirectory = new File(corePlugin.getDataFolder() + File.separator + "userdatas");
    userData = loadData();
    onlinePlayer = new Unit<>(Bukkit.getPlayer(uuid));
  }

  public void save() {

    final IWriter writer;

    if (SAVE_DATA_IN_DATABASE) {
      writer = new MongoDataWriter();
    } else {
      writer = new GsonFileWriter(corePlugin, getUserDataDirectory(), uuid.toString());
    }
    writer.write(getUserData());
  }

  private UserData loadData() {

    final IReader reader;

    if (SAVE_DATA_IN_DATABASE) {
      reader = new MongoDataReader();
    } else {
      reader = new GsonFileReader(corePlugin, getUserDataDirectory(), uuid.toString());
    }

    return (UserData) reader.read(UserData.class, getDefaultUserData());
  }

  private UserData getDefaultUserData() {
    return new UserData(); // everything set to 0 and new
  }

}
