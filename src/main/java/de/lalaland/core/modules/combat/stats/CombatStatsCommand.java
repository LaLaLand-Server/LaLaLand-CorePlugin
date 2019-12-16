package de.lalaland.core.modules.combat.stats;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.combat.CombatModule;
import de.lalaland.core.ui.Message;
import de.lalaland.core.utils.common.UtilMath;
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

  public CombatStatsCommand(final CombatModule module) {
    combatStatManager = module.getCombatStatManager();
  }

  private final CombatStatManager combatStatManager;

  @Default
  public void onStatsCommand(final Player sender) {
    // TODO open GUI with stats
  }

  @Subcommand("mana show")
  public void onMana(final Player sender) {
    final CombatStatHolder holder = combatStatManager.getCombatStatHolder(sender);
    final int maxMana = (int) holder.getStatValue(CombatStat.MANA);
    final int mana = holder.getMana();
    Message.send(sender, CombatModule.class, "" + mana + " / " + maxMana + " Mana");
  }

  @Subcommand("mana remove")
  public void onManaRemove(final Player sender, final int amount) {
    final CombatStatHolder holder = combatStatManager.getCombatStatHolder(sender);
    holder.removeMana(amount);
  }

  @Subcommand("mana add")
  public void onManaShow(final Player sender, final int amount) {
    final CombatStatHolder holder = combatStatManager.getCombatStatHolder(sender);
    holder.addMana(amount);
  }

  @Subcommand("chat")
  public void onStatsChat(final Player sender) {
    final CombatStatHolder holder = combatStatManager.getCombatStatHolder(sender);

    sender.sendMessage("§eKampf Stats: ");

    for (final CombatStat stat : CombatStat.values()) {
      double baseValue = holder.getStatBaseValue(stat);
      double extraValue = holder.getStatExtraValue(stat);
      double fullValue = baseValue + extraValue;
      baseValue = UtilMath.cut(baseValue, 1);
      extraValue = UtilMath.cut(extraValue, 1);
      fullValue = UtilMath.cut(fullValue, 1);

      final String line =
          "§f- " + "§9" + stat.getDisplayName() + " §f>> §e" + fullValue + " [§7" + baseValue + " + " + extraValue
              + "§e]";
      sender.sendMessage(line);
    }

  }

}
