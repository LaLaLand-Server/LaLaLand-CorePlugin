package de.lalaland.core.modules.skills.skillimpl;

import de.lalaland.core.modules.combat.api.EntityKillEntityEvent;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.modules.combat.stats.CombatStatManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
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

  @EventHandler
  public void onDeath(final EntityDeathEvent event) {
    combatStatManager.getCombatStatHolder(event.getEntity()).castSkills(SkillTrigger.DEATH);
  }

  @EventHandler
  public void onFall(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof LivingEntity)) {
      return;
    }
    if (!(event.getCause() != DamageCause.FALL)) {
      return;
    }
    combatStatManager.getCombatStatHolder((LivingEntity) event.getEntity()).castSkills(SkillTrigger.FALL);
  }

  @EventHandler
  public void onKill(final EntityKillEntityEvent event) {
    final CombatStatHolder attackerHolder = event.getCombatContext().getAttackerHolder();
    if (attackerHolder != null) {
      attackerHolder.castSkills(SkillTrigger.KILL);
    }
  }

}