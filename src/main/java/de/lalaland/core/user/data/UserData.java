package de.lalaland.core.user.data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin neuraxhd@gmail.com
 *
 * This file is part of CorePlugin and was created at the 13.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

@Data
@AllArgsConstructor
public class UserData {

  public static long[] NEED_EXP_LEVEL = new long[]{0, 100, 250, 575, 1050, 2250, 5000, 7500, 10000,
      150000};

  private int level;
  private long exp;
  private int moneyOnHand;
  private int moneyOnBank;
  private List<String> offlineMessages;

  public boolean canLevelup() {
    return exp >= getNeedExp();
  }

  public void increaseLevel() {
    level += 1;
  }

  public void addExp(final long amount) {
    exp += amount;
  }

  public boolean isMaxLevel() {
    return level >= NEED_EXP_LEVEL.length;
  }

  public long getNeedExp() {
    return NEED_EXP_LEVEL[level];
  }


}
