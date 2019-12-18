package de.lalaland.core.modules.protection.regions.editor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.lalaland.core.modules.protection.ProtectionModule;
import de.lalaland.core.modules.protection.regions.Permit;
import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import de.lalaland.core.modules.protection.regions.RegionManager;
import de.lalaland.core.modules.protection.regions.RegionRule;
import de.lalaland.core.modules.protection.regions.Relation;
import de.lalaland.core.modules.protection.regions.RuleSet;
import de.lalaland.core.utils.Message;
import java.util.Set;
import org.bukkit.block.Block;
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

  public RegionCommand(final RegionManager regionManager) {
    this.regionManager = regionManager;
  }

  private final RegionManager regionManager;

  @Default
  public void onRegion(final Player sender) {
    sender.sendMessage("§e -------- Regionen --------");
    final Set<ProtectedRegion> regionSet = regionManager.getRegionsAt(sender.getLocation());
    final int regionAmount = regionSet == null ? 0 : regionSet.size();
    sender.sendMessage("§eAnzahl der Regionen: §9" + regionAmount);
    final ProtectedRegion region = regionManager.getMostRelevantRegion(sender.getLocation());
    final String regionName = region == null ? "§cKeine" : "§9" + region.getName();
    sender.sendMessage("§eWichtigste Region: " + regionName);
    sender.sendMessage("");
    if (region == null) {
      return;
    }
    sender.sendMessage("§eRegeln:");
    sender.sendMessage("§fFreunde §7| §fFeinde §7| §fNeutral");

    final RuleSet ruleSet = region.getRuleSet();

    for (final RegionRule rule : RegionRule.values()) {
      final StringBuilder builder = new StringBuilder("§e" + rule.toString());
      final int index = 0;
      for (final Relation relation : Relation.values()) {
        final Permit permit = ruleSet.getPermit(relation, rule);
        final String color = permit == Permit.DENY ? "§c" : "§a";

      }
      sender.sendMessage(builder.toString());
    }

  }

  @Subcommand("test")
  public void onRegionCreate(final Player sender, final int size) {
    final Block loc1 = sender.getLocation().getBlock().getRelative(size, size, size);
    final Block loc2 = sender.getLocation().getBlock().getRelative(-size, -size, -size);
    final ProtectedRegion region = regionManager.createRegion(loc1, loc2);
    region.getRuleSet().applyRule(Relation.NEUTRAL, RegionRule.BLOCK_BREAK, Permit.DENY);
    region.getRuleSet().applyRule(Relation.NEUTRAL, RegionRule.BLOCK_PLACE, Permit.DENY);
    Message.send(sender, ProtectionModule.class, "Region wurde erstellt.");
  }

}
