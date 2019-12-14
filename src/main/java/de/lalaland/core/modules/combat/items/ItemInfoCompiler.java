package de.lalaland.core.modules.combat.items;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.jobs.JobModule;
import de.lalaland.core.modules.jobs.jobdata.JobDataManager;
import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.user.data.UserData;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.items.display.DisplayConverter;
import de.lalaland.core.utils.items.display.ItemInfo;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Map;
import java.util.UUID;
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

  public ItemInfoCompiler(final CorePlugin plugin) {
    userManager = plugin.getUserManager();
    jobDataManager = plugin.getModule(JobModule.class).getJobDataManager();
  }

  private final UserManager userManager;
  private final JobDataManager jobDataManager;

  @Override
  public String getDisplayKey() {
    return NBT_VALUE;
  }

  @Override
  public ItemInfo compileInfo(final Player player, final ItemStack item) {
    final UUID userID = player.getUniqueId();
    final ItemInfo info = new ItemInfo(item);
    final StatItem statItem = StatItem.of(item);
    final int levelReq = statItem.getLevelRequirement();

    if (levelReq > 0) {
      final UserData data = userManager.getUser(userID).getUserData();
      final String color = statItem.canUse(data) ? "§a" : "§c";
      info.addLore("");
      info.addLore("              §eItem Level " + color + levelReq);
    }

    final Map<CombatStat, Double> statMap = statItem.getCombatStatMap();
    if (statMap != null && !statMap.isEmpty()) {
      info.addLore("");
      info.addLore("§e------------ Stats ------------");
      info.addLore("");
      statMap.forEach((stat, value) -> {
        final String extra = stat.isPercentageStyle() ? "%" : "";
        info.addLore("§f    " + stat.getModel().getChar() + " §9> §f" + value + extra);
      });
    }

    final Object2IntMap<JobType> jobReqMap = statItem.getJobRequirements();
    final Object2DoubleMap<JobType> jobValMap = statItem.getJobValues();
    if (!(jobReqMap == null && jobValMap == null)) {
      info.addLore("");
      info.addLore("§e------------ Jobs ------------");
      info.addLore("");
      if (jobReqMap != null) {
        for (final Entry<JobType> entry : jobReqMap.object2IntEntrySet()) {
          final JobType job = entry.getKey();
          final int lvl = entry.getIntValue();
          final String color = jobDataManager.getHolder(userID).getJobData(job).hasLevel(lvl) ? "§a" : "§c";
          info.addLore("§f" + job.getDisplayName() + "-Level §9> " + color + lvl);
        }
      }
      if (jobValMap != null) {
        for (final Object2DoubleMap.Entry<JobType> entry : jobValMap.object2DoubleEntrySet()) {
          info.addLore("§f" + entry.getKey().getDisplayName() + "-Kraft §9> §f" + entry.getDoubleValue());
        }
      }
    }

    final Integer dura = statItem.getDurability();
    final Integer maxDura = statItem.getMaxDurability();
    if (dura != null && maxDura != null) {
      info.addLore("");
      info.addLore("§7Haltbarkeit");
      //info.addLore(UtilMath.getPercentageBar(dura, maxDura, 45, "∎"));
      info.addLore("§7" + dura + " / " + maxDura + " §f" + UtilMath.getHPBarAsChar(dura, maxDura));
    }

    return info;
  }
}
