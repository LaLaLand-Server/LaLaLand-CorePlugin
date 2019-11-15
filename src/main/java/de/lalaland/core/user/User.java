package de.lalaland.core.user;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.io.IReader;
import de.lalaland.core.io.IWriter;
import de.lalaland.core.io.gson.GsonFileReader;
import de.lalaland.core.io.gson.GsonFileWriter;
import de.lalaland.core.io.mongodb.MongoDataReader;
import de.lalaland.core.io.mongodb.MongoDataWriter;
import de.lalaland.core.user.data.UserData;
import de.lalaland.core.utils.tuples.Unit;
import java.io.File;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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

  private final CorePlugin corePlugin;
  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean updateCandidate;
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
    updateCandidate = false;
  }

  public void save() {

    final IWriter writer;

    if (corePlugin.getCoreConfig().isSaveDataInDatabase()) {
      writer = new MongoDataWriter();
    } else {
      writer = new GsonFileWriter(corePlugin, getUserDataDirectory(), uuid.toString());
    }
    writer.write(getUserData());
    setUpdateCandidate(false);
  }

  private UserData loadData() {

    final IReader reader;

    if (corePlugin.getCoreConfig().isSaveDataInDatabase()) {
      reader = new MongoDataReader();
    } else {
      reader = new GsonFileReader(corePlugin, getUserDataDirectory(), uuid.toString());
    }

    return (UserData) reader.read(UserData.class, getDefaultUserData());
  }

  public void addExp(final long amount) {

    userData.addExp(amount);

    if (userData.canLevelup()) {
      userData.increaseLevel();
      //TODO: Send player message
    }
    setUpdateCandidate(true);
  }

  private UserData getDefaultUserData() {
    return new UserData(1, 0); // everything set to 0 and new
  }

}
