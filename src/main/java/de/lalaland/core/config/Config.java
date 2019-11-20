package de.lalaland.core.config;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Config {

  @Getter
  @SerializedName("UseDB")
  private final boolean saveDataInDatabase;
  @Getter
  @SerializedName("UnusedUserRemoverInterval")
  private final int unusedUserRemoverInterval;
  @Getter
  @SerializedName("UserSaveInterval")
  private final int userSaveInterval;
  @Getter
  @SerializedName("FillGuis")
  private final boolean fillGuis;

  static Config getDefaultConfig() {
    return new Config(false,
        5, 10, true);
  }

}
