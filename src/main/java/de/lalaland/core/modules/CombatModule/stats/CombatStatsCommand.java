package de.lalaland.core.modules.CombatModule.stats;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.google.common.collect.ImmutableMap;
import de.lalaland.core.modules.CombatModule.CombatModule;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.tuples.Pair;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("stats")
@CommandPermission("user")
public class CombatStatsCommand extends BaseCommand {

  public CombatStatsCommand(CombatModule module) {
    this.combatStatManager = module.getCombatStatManager();
  }

  private final CombatStatManager combatStatManager;

  @Default
  public void onStatsCommand(Player sender) {
    // TODO open GUI with stats
  }

  @Subcommand("chat")
  public void onStatsChat(Player sender) {
    CombatStatHolder holder = this.combatStatManager.getCombatStatHolder(sender);
    Pair<ImmutableMap<CombatStat, Double>, ImmutableMap<CombatStat, Double>> maps = holder
        .getValueMappings();

    sender.sendMessage("§eKampf Stats: ");

    for (CombatStat stat : CombatStat.values()) {
      double baseValue = maps.getLeft().get(stat);
      double fullValue = maps.getRight().get(stat);
      double delta = fullValue - baseValue;
      baseValue = UtilMath.cut(baseValue, 1);
      fullValue = UtilMath.cut(fullValue, 1);
      delta = UtilMath.cut(delta, 1);

      String name =
          "§9" + stat.getDisplayName() + " >> §e" + fullValue + " [" + baseValue + " + " + delta
              + "]";
      sender.sendMessage(name);
    }

  }

}
