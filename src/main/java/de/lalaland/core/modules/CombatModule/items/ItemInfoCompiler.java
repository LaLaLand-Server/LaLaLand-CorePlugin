package de.lalaland.core.modules.CombatModule.items;

import de.lalaland.core.modules.CombatModule.stats.CombatStat;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.items.display.DisplayConverter;
import de.lalaland.core.utils.items.display.ItemInfo;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemInfoCompiler implements DisplayConverter {

  public static final String NBT_VALUE = "ItemDisplay";

  @Override
  public String getDisplayKey() {
    return NBT_VALUE;
  }

  @Override
  public ItemInfo compileInfo(Player player, ItemStack item) {

    ItemInfo info = new ItemInfo(item);
    StatItem statItem = StatItem.of(item);
    Map<CombatStat, Double> statMap = statItem.getCombatStatMap();
    if (statMap != null && !statMap.isEmpty()) {
      info.addLore("");
      info.addLore("§e------------ Stats ------------");
      statMap.forEach((stat, value) -> {
        String extra = stat.isPercentageStyle() ? "%" : "";
        info.addLore("§9" + stat.getDisplayName() + "> §f" + value + extra);
      });
    }

    Integer dura = statItem.getDurability();
    Integer maxDura = statItem.getMaxDurability();
    if (dura != null && maxDura != null) {
      info.addLore("");
      info.addLore("§7Haltbarkeit> " + dura + " / " + maxDura);
      info.addLore(UtilMath.getPercentageBar(dura, maxDura, 20, "∎"));
    }

    return info;
  }
}
