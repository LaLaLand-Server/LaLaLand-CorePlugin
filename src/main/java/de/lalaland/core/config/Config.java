package de.lalaland.core.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class Config {

  @Getter
  @SerializedName("UseDB")
  private final boolean saveDataInDatabase;
  @Getter
  @SerializedName("UnusedUserRemoverInterval")
  private final int unusedUserRemoverInterval;

  Config(final boolean saveDataInDatabase, final int unusedUserRemoverInterval) {
    this.saveDataInDatabase = saveDataInDatabase;
    this.unusedUserRemoverInterval = unusedUserRemoverInterval;
  }


  static Config getDefaultConfig(){
    return new Config(false,5);
  }

}
