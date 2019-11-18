package de.lalaland.core.modules.combat.stats;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.lalaland.core.modules.combat.items.StatItem;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.holograms.MovingHologram;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

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

  private static final int HOLOGRAM_LIFE_TICKS = 30;
  private static final Vector BASE_HOLOGRAM_VELOCITY = new Vector(0, 0.075, 0);
  private static final Vector BASE_SCALAR_XZ = new Vector(1, 0, 1);
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

  public CombatDamageListener(CombatStatManager combatStatManager,
      HologramManager hologramManager) {
    this.combatStatManager = combatStatManager;
    this.critChanceRandom = ThreadLocalRandom.current();
    this.hologramManager = hologramManager;
  }

  private final CombatStatManager combatStatManager;
  private final ThreadLocalRandom critChanceRandom;
  private final HologramManager hologramManager;

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
    LivingEntity attackerLiving = (LivingEntity) attacker;
    CombatStatHolder attackHolder = combatStatManager.getCombatStatHolder(attackerLiving);
    CombatStatHolder defenceHolder = combatStatManager.getCombatStatHolder(defenderLiving);
    boolean isPlayerAttacker = attacker instanceof Player;

    event.setDamage(0);
    double damage = attackHolder
        .getStatValue(isRanged ? CombatStat.RANGE_DAMAGE : CombatStat.MEELE_DAMAGE);

    boolean crit = attackHolder.getStatValue(CombatStat.CRIT_CHANCE) >= this.critChanceRandom
        .nextDouble(0, 100);

    if (crit) {
      double dmgMulti = (1D / 100D) * attackHolder.getStatValue(CombatStat.CRIT_DAMAGE);
      damage *= dmgMulti;
    }

    damage = DamageEvaluator
        .calculateDamage(defenceHolder, damage, CombatDamageType.ofBukkit(event.getCause()));

    ItemStack attackItem = attackerLiving.getActiveItem();
    if (isPlayerAttacker) {
      float multi = UtilPlayer.getAttackCooldown((Player)attacker);
      if(multi < 0.9F){
        damage *= 0.05;
      }else{
        damage *= multi;
      }
      attackItem = ((Player) attacker).getInventory().getItemInMainHand();
    }
    if (attackItem != null && attackItem.getType() != Material.AIR) {
      StatItem statItem = StatItem.of(attackItem);
      if (statItem.isItemStatComponent()) {
        int durability = statItem.getDurability();
        Preconditions.checkState(durability >= 0, "Item durability is below 0");
        if (durability == 0) {
          damage *= 0.025;
        } else if (durability > 0) {
          statItem.setDurability(durability - 1);
          if(isPlayerAttacker){
            ((Player) attacker).getInventory().setItemInMainHand(statItem.getItemStack());
          }else{
            attackerLiving.getEquipment().setItemInMainHand(statItem.getItemStack());
          }
        }
      }
    }

    damage = UtilMath.cut(damage, 1);

    if (isPlayerAttacker) {
      Player attackerPlayer = (Player) attacker;
      this.createDamageHologram(attackerPlayer, defender, crit, damage);
    }

    double healthLeft = defenderLiving.getHealth() - damage;
    if (healthLeft <= 0) {
      healthLeft = 0;
    }
    defenderLiving.setHealth(healthLeft);

  }

  private void createDamageHologram(Player attackerPlayer, Entity def, boolean crit, double dmg) {
    String holoMsg = (crit ? "§c" : "§e") + dmg;
    Location defLoc = def.getLocation().clone().add(0, 0.5, 0);
    Vector directionAdjustVec = attackerPlayer.getLocation().getDirection().clone()
        .multiply(BASE_SCALAR_XZ).normalize().multiply(0.05);
    Vector holoVel = BASE_HOLOGRAM_VELOCITY.clone().add(directionAdjustVec);
    MovingHologram moving = this.hologramManager
        .createMovingHologram(defLoc, holoVel, HOLOGRAM_LIFE_TICKS);
    moving.getHologram().appendTextLine(holoMsg);
  }

}
