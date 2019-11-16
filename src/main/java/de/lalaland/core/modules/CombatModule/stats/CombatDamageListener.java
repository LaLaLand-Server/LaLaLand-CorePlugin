package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.ImmutableMap;
import de.lalaland.core.utils.common.UtilMath;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatDamageListener implements Listener {

  private static final ImmutableMap<DamageCause, Double> ENVIRONMENTAL_BASE_PERCENTAGE = ImmutableMap.<DamageCause, Double>builder()
      .put(DamageCause.BLOCK_EXPLOSION, 25D)
      .put(DamageCause.CONTACT, 2.5D)
      .put(DamageCause.CRAMMING, 15D)
      .put(DamageCause.DRAGON_BREATH, 20D)
      .put(DamageCause.DROWNING, 33.33D)
      .put(DamageCause.DRYOUT, 50D)
      .put(DamageCause.ENTITY_EXPLOSION, 25D)
      .put(DamageCause.FALL, 12.5D)
      .put(DamageCause.FALLING_BLOCK, 15D)
      .put(DamageCause.FIRE, 17.5D)
      .put(DamageCause.FIRE_TICK, 10D)
      .put(DamageCause.FLY_INTO_WALL, 50D)
      .put(DamageCause.HOT_FLOOR, 9D)
      .put(DamageCause.LAVA, 20D)
      .put(DamageCause.LIGHTNING, 50D)
      .put(DamageCause.MAGIC, 15D)
      .put(DamageCause.MELTING, 33.33D)
      .put(DamageCause.POISON, 7.5D)
      .put(DamageCause.PROJECTILE, 10D)
      .put(DamageCause.STARVATION, 50D)
      .put(DamageCause.SUFFOCATION, 45D)
      .put(DamageCause.SUICIDE, 100D)
      .put(DamageCause.THORNS, 7.5D)
      .put(DamageCause.VOID, 50D)
      .put(DamageCause.WITHER, 8.5D)
      .build();

  public CombatDamageListener(CombatStatManager combatStatManager) {
    this.combatStatManager = combatStatManager;
  }

  private final CombatStatManager combatStatManager;

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      return;
    }
    Entity defender = event.getEntity();
    if (!(defender instanceof LivingEntity)) {
      return;
    }
    LivingEntity defenderLiving = (LivingEntity) defender;
    double damage = event.getDamage() * ENVIRONMENTAL_BASE_PERCENTAGE.get(event.getCause());
    damage *= (100D / defenderLiving.getHealth());
    CombatStatHolder holderDefender = this.combatStatManager
        .getCombatStatHolder(defender.getUniqueId());
    damage = DamageEvaluator
        .calculateDamage(holderDefender, damage, CombatDamageType.ofBukkit(event.getCause()));

    event.setDamage(0);

    defenderLiving.setHealth(defenderLiving.getHealth() - damage);

  }

  @EventHandler
  public void onCombat(EntityDamageByEntityEvent event) {
    Entity attacker = event.getDamager();
    Entity defender = event.getEntity();

    boolean isRanged = false;

    if (attacker instanceof Projectile) {
      isRanged = true;
      ProjectileSource source = ((Projectile) attacker).getShooter();
      if (source instanceof Entity) {
        attacker = (Entity) source;
      } else {
        //TODO Evaluate random projectiles
      }
    }

    if (!(attacker instanceof LivingEntity && defender instanceof LivingEntity)) {
      return;
    }

    LivingEntity defenderLiving = (LivingEntity) defender;
    CombatStatHolder attackHolder = combatStatManager.getCombatStatHolder((LivingEntity) attacker);
    CombatStatHolder defenceHolder = combatStatManager.getCombatStatHolder(defenderLiving);

    event.setDamage(0);
    double damage = attackHolder
        .getStatValue(isRanged ? CombatStat.MEELE_DAMAGE : CombatStat.RANGE_DAMAGE);

    damage = DamageEvaluator
        .calculateDamage(defenceHolder, damage, CombatDamageType.ofBukkit(event.getCause()));

    damage = UtilMath.cut(damage, 1);

    defenderLiving.setHealth(defenderLiving.getHealth() - damage);

  }

}
