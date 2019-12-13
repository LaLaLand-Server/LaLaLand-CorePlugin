package de.lalaland.core.utils.common;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import de.lalaland.core.modules.resourcepack.skins.Model;

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

  public static void initHPMap() {
    final Range<Double> r0 = Range.atMost(0.0D);
    final Range<Double> r0_5 = Range.openClosed(0.0D, 0.05D);
    final Range<Double> r5_10 = Range.openClosed(0.05D, 0.10D);
    final Range<Double> r10_15 = Range.openClosed(0.10D, 0.15D);
    final Range<Double> r15_20 = Range.openClosed(0.15D, 0.20D);
    final Range<Double> r20_25 = Range.openClosed(0.20D, 0.25D);
    final Range<Double> r25_30 = Range.openClosed(0.25D, 0.30D);
    final Range<Double> r30_35 = Range.openClosed(0.30D, 0.35D);
    final Range<Double> r35_40 = Range.openClosed(0.35D, 0.40D);
    final Range<Double> r40_45 = Range.openClosed(0.40D, 0.45D);
    final Range<Double> r45_50 = Range.openClosed(0.45D, 0.50D);
    final Range<Double> r50_55 = Range.openClosed(0.50D, 0.55D);
    final Range<Double> r55_60 = Range.openClosed(0.55D, 0.60D);
    final Range<Double> r60_65 = Range.openClosed(0.60D, 0.65D);
    final Range<Double> r65_70 = Range.openClosed(0.65D, 0.70D);
    final Range<Double> r70_75 = Range.openClosed(0.70D, 0.75D);
    final Range<Double> r75_80 = Range.openClosed(0.75D, 0.80D);
    final Range<Double> r80_85 = Range.openClosed(0.80D, 0.85D);
    final Range<Double> r85_90 = Range.openClosed(0.85D, 0.90D);
    final Range<Double> r90_95 = Range.open(0.90D, 0.95D);
    final Range<Double> r95_100 = Range.atLeast(1D);

    HP_BAR_BASE_MAP.put(r0, Model.HP_0.getChar());
    HP_BAR_BASE_MAP.put(r0_5, Model.HP_5.getChar());
    HP_BAR_BASE_MAP.put(r5_10, Model.HP_10.getChar());
    HP_BAR_BASE_MAP.put(r10_15, Model.HP_15.getChar());
    HP_BAR_BASE_MAP.put(r15_20, Model.HP_20.getChar());
    HP_BAR_BASE_MAP.put(r20_25, Model.HP_25.getChar());
    HP_BAR_BASE_MAP.put(r25_30, Model.HP_30.getChar());
    HP_BAR_BASE_MAP.put(r30_35, Model.HP_35.getChar());
    HP_BAR_BASE_MAP.put(r35_40, Model.HP_40.getChar());
    HP_BAR_BASE_MAP.put(r40_45, Model.HP_45.getChar());
    HP_BAR_BASE_MAP.put(r45_50, Model.HP_50.getChar());
    HP_BAR_BASE_MAP.put(r50_55, Model.HP_55.getChar());
    HP_BAR_BASE_MAP.put(r55_60, Model.HP_60.getChar());
    HP_BAR_BASE_MAP.put(r60_65, Model.HP_65.getChar());
    HP_BAR_BASE_MAP.put(r65_70, Model.HP_70.getChar());
    HP_BAR_BASE_MAP.put(r70_75, Model.HP_75.getChar());
    HP_BAR_BASE_MAP.put(r75_80, Model.HP_80.getChar());
    HP_BAR_BASE_MAP.put(r80_85, Model.HP_85.getChar());
    HP_BAR_BASE_MAP.put(r85_90, Model.HP_90.getChar());
    HP_BAR_BASE_MAP.put(r90_95, Model.HP_95.getChar());
    HP_BAR_BASE_MAP.put(r95_100, Model.HP_100.getChar());

    PROGRESS_BAR_MAP.put(r0, Model.PROGRESS_0.getChar());
    PROGRESS_BAR_MAP.put(r0_5, Model.PROGRESS_5.getChar());
    PROGRESS_BAR_MAP.put(r5_10, Model.PROGRESS_10.getChar());
    PROGRESS_BAR_MAP.put(r10_15, Model.PROGRESS_15.getChar());
    PROGRESS_BAR_MAP.put(r15_20, Model.PROGRESS_20.getChar());
    PROGRESS_BAR_MAP.put(r20_25, Model.PROGRESS_25.getChar());
    PROGRESS_BAR_MAP.put(r25_30, Model.PROGRESS_30.getChar());
    PROGRESS_BAR_MAP.put(r30_35, Model.PROGRESS_35.getChar());
    PROGRESS_BAR_MAP.put(r35_40, Model.PROGRESS_40.getChar());
    PROGRESS_BAR_MAP.put(r40_45, Model.PROGRESS_45.getChar());
    PROGRESS_BAR_MAP.put(r45_50, Model.PROGRESS_50.getChar());
    PROGRESS_BAR_MAP.put(r50_55, Model.PROGRESS_55.getChar());
    PROGRESS_BAR_MAP.put(r55_60, Model.PROGRESS_60.getChar());
    PROGRESS_BAR_MAP.put(r60_65, Model.PROGRESS_65.getChar());
    PROGRESS_BAR_MAP.put(r65_70, Model.PROGRESS_70.getChar());
    PROGRESS_BAR_MAP.put(r70_75, Model.PROGRESS_75.getChar());
    PROGRESS_BAR_MAP.put(r75_80, Model.PROGRESS_80.getChar());
    PROGRESS_BAR_MAP.put(r80_85, Model.PROGRESS_85.getChar());
    PROGRESS_BAR_MAP.put(r85_90, Model.PROGRESS_90.getChar());
    PROGRESS_BAR_MAP.put(r90_95, Model.PROGRESS_95.getChar());
    PROGRESS_BAR_MAP.put(r95_100, Model.PROGRESS_100.getChar());

  }

  private static final RangeMap<Double, Character> HP_BAR_BASE_MAP = TreeRangeMap.create();
  private static final RangeMap<Double, Character> PROGRESS_BAR_MAP = TreeRangeMap.create();

  public static Character getHPBarAsChar(final double hpPercent) {
    return HP_BAR_BASE_MAP.get(hpPercent);
  }

  public static Character getHPBarAsChar(final double current, final double max) {
    return getHPBarAsChar(1D / max * current);
  }

  public static Character getProgressBarAsChar(final double progressPercent) {
    return PROGRESS_BAR_MAP.get(progressPercent);
  }

  public static Character getProgressBarAsChar(final double current, final double max) {
    return getProgressBarAsChar(1D / max * current);
  }

  public static double cut(double value, final int decimalPoints) {
    final int decades = (int) Math.pow(10, decimalPoints);
    value = ((double) ((int) (value * decades))) / 10;
    return value;
  }

  public static int round(final double value) {
    return (int) (value + 0.5);
  }

  public static String getPercentageBar(final double current, final double max, final int size, final String segment) {
    final StringBuilder builder = new StringBuilder();
    int lows = (int) (size * ((1D / max) * current));
    int highs = size - lows;
    builder.append("§a");
    while (lows > 0) {
      builder.append(segment);
      lows--;
    }
    builder.append("§c");
    while (highs > 0) {
      builder.append(segment);
      highs--;
    }
    return builder.toString();
  }

}
