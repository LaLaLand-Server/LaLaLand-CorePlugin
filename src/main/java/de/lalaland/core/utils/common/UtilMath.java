package de.lalaland.core.utils.common;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilMath {

  public static double cut(double value, int decimalPoints) {
    int decades = (int) Math.pow(10, decimalPoints);
    value = ((double) ((int) (value * decades))) / 10;
    return value;
  }

  public static int round(double value) {
    return (int)(value + 0.5);
  }

}
