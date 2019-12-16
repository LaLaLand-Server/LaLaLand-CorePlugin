package de.lalaland.core.modules.skills.skillimpl;

import de.lalaland.core.modules.combat.stats.CombatStatManager;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 15.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class SkillTriggerListener implements Listener {

  private final CombatStatManager combatStatManager;

  @EventHandler
  public void onCrouch(final PlayerToggleSneakEvent event) {
    combatStatManager.getCombatStatHolder(event.getPlayer()).castSkills(SkillTrigger.CROUCH);
  }

}