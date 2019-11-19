package de.lalaland.core.modules.protection.regions.editor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import de.lalaland.core.modules.protection.regions.Permit;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.protection.regions.RegionRule;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.modules.protection.regions.RuleSet;
import java.util.Set;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("region")
@CommandPermission("admin")
public class RegionCommand extends BaseCommand {

  public RegionCommand(RegionManager regionManager) {
    this.regionManager = regionManager;
  }

  private final RegionManager regionManager;

  @Default
  public void onRegion(Player sender) {
    sender.sendMessage("§e -------- Regionen --------");
    Set<ProtectedRegion> regionSet = this.regionManager.getRegionsAt(sender.getLocation());
    int regionAmount = regionSet == null ? 0 : regionSet.size();
    sender.sendMessage("§eAnzahl der Regionen: §9" + regionAmount);
    ProtectedRegion region = this.regionManager.getMostRelevantRegion(sender.getLocation());
    String regionName = region == null ? "§cKeine" : "§9" + region.getName();
    sender.sendMessage("§eWichtigste Region: " + regionName);
    sender.sendMessage("");
    if (region == null) {
      return;
    }
    sender.sendMessage("§eRegeln:");
    sender.sendMessage("§eFreunde | Feinde | Neutral");

    RuleSet ruleSet = region.getRuleSet();

    for (RegionRule rule : RegionRule.values()) {
      StringBuilder builder = new StringBuilder("§e" + rule.toString());
      int index = 0;
      for (Relation relation : Relation.values()) {
        Permit permit = ruleSet.getPermit(relation, rule);
        String color = permit == Permit.DENY ? "§c" : "§a";

      }
    }

  }

}
