package de.lalaland.core.modules.combat.stats;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.combat.items.StatItem;
import de.lalaland.core.modules.combat.items.WeaponType;
import de.lalaland.core.utils.UtilModule;
import de.lalaland.core.utils.common.UtilMath;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.holograms.MovingHologram;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import de.lalaland.core.utils.holograms.infobars.AbstractInfoBar;
import de.lalaland.core.utils.holograms.infobars.InfoBarManager;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
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

  public CombatDamageListener(final CombatStatManager combatStatManager,
      final HologramManager hologramManager, final CorePlugin plugin) {
    this.combatStatManager = combatStatManager;
    critChanceRandom = ThreadLocalRandom.current();
    this.hologramManager = hologramManager;
    critKey = new NamespacedKey(plugin, "crit");
    dmgKey = new NamespacedKey(plugin, "damage");
    random = ThreadLocalRandom.current();
    infoBarManager = plugin.getModule(UtilModule.class).getInfoBarManager();
  }

  private final InfoBarManager infoBarManager;
  private final CombatStatManager combatStatManager;
  private final ThreadLocalRandom critChanceRandom;
  private final HologramManager hologramManager;
  private final NamespacedKey critKey;
  private final NamespacedKey dmgKey;
  final ThreadLocalRandom random;

  @EventHandler
  public void onDamage(final EntityDamageEvent event) {
    if (event instanceof EntityDamageByEntityEvent) {
      return;
    }
    final Entity defender = event.getEntity();
    if (!(defender instanceof LivingEntity)) {
      return;
    }
    final LivingEntity defenderLiving = (LivingEntity) defender;
    double damage = event.getDamage() * ENVIRONMENTAL_BASE_PERCENTAGE.get(event.getCause());
    damage *= (100D / defenderLiving.getHealth());
    final CombatStatHolder holderDefender = combatStatManager
        .getCombatStatHolder(defender.getUniqueId());
    damage = DamageEvaluator
        .calculateDamage(holderDefender, damage, CombatDamageType.ofBukkit(event.getCause()));

    event.setDamage(0);

    double healthLeft = defenderLiving.getHealth() - damage;
    if (healthLeft < 0) {
      healthLeft = 0;
    }

    defenderLiving.setHealth(healthLeft);

    final List<Entity> pass = defenderLiving.getPassengers();
    if (!pass.isEmpty()) {
      final Entity token = pass.get(0);
      if (token.getType() == EntityType.ARMOR_STAND) {
        final AbstractInfoBar bar = infoBarManager.getInfoBar(pass.get(0));
        if (bar == null) {
          return;
        }
        bar.setLine(0, "" + UtilMath.getHPBarAsChar(defenderLiving.getHealth(), holderDefender.getStatValue(CombatStat.HEALTH)));
      }
    }
  }

  @EventHandler
  public void onHeal(final EntityRegainHealthEvent event) {
    if (!(event.getEntity() instanceof LivingEntity)) {
      return;
    }
    final LivingEntity defenderLiving = (LivingEntity) event.getEntity();
    final CombatStatHolder holderDefender = combatStatManager.getCombatStatHolder(defenderLiving.getUniqueId());

    createHealingHologram((LivingEntity) event.getEntity(), UtilMath.cut(event.getAmount(), 1));

    final List<Entity> pass = defenderLiving.getPassengers();
    if (!pass.isEmpty()) {
      final Entity token = pass.get(0);
      if (token.getType() == EntityType.ARMOR_STAND) {
        final AbstractInfoBar bar = infoBarManager.getInfoBar(pass.get(0));
        if (bar == null) {
          return;
        }
        bar.setLine(0, "" + UtilMath.getHPBarAsChar(defenderLiving.getHealth(), holderDefender.getStatValue(CombatStat.HEALTH)));
      }
    }
  }

  @EventHandler
  public void onSwing(final PlayerAnimationEvent event) {
    final Player player = event.getPlayer();
    if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) {
      return;
    }
    final ItemStack item = player.getInventory().getItemInMainHand();
    if (item == null || item.getType() == Material.AIR) {
      return;
    }

    final StatItem statItem = StatItem.of(item);
    final WeaponType weaponType = statItem.getWeaponType();
    if (weaponType == null || weaponType.isMeele()) {
      return;
    }

    // TODO Quiver with Arrow.class etc

    float cool = UtilPlayer.getAttackCooldown(player);
    if (cool < 0.5) {
      UtilPlayer.playSound(player, Sound.ITEM_CROSSBOW_LOADING_MIDDLE);
      return;
    }

    if (cool < 0.95) {
      cool = 0.4F;
      UtilPlayer.playSound(player, Sound.ENTITY_ARROW_SHOOT, 1.0F, 0.75F);
    } else {
      UtilPlayer.playSound(player, Sound.ENTITY_ARROW_SHOOT);
    }

    double speed = 1D;

    final Vector direction = player.getEyeLocation().getDirection();

    double xOff = 0;
    double yOff = 0;
    double zOff = 0;

    switch (weaponType) {
      case SHORT_BOW:
        speed = 2.75;
        xOff = random.nextDouble(-0.032, 0.032);
        yOff = random.nextDouble(-0.032, 0.032);
        zOff = random.nextDouble(-0.032, 0.032);
        break;
      case LONG_BOW:
        speed = 3.6;
        break;
      default:
        break;
    }

    speed *= cool;

    UtilPlayer.playSound(player, Sound.ENTITY_ARROW_SHOOT);
    final Arrow arrow = player.launchProjectile(Arrow.class, direction.add(new Vector(xOff, yOff, zOff)).multiply(speed));

  }

  @EventHandler
  public void onProjectile(final ProjectileLaunchEvent event) {
    final Projectile projectile = event.getEntity();
    final ProjectileSource source = projectile.getShooter();
    if (source instanceof LivingEntity) {
      final LivingEntity attacker = (LivingEntity) source;
      final CombatStatHolder holder = combatStatManager.getCombatStatHolder(attacker);
      double dmg = holder.getStatBaseValue(CombatStat.RANGE_DAMAGE);
      final boolean crit = holder.getStatValue(CombatStat.CRIT_CHANCE) >= critChanceRandom.nextDouble(0, 100);
      if (crit) {
        final double dmgMulti = (1D / 100D) * holder.getStatValue(CombatStat.CRIT_DAMAGE);
        dmg *= dmgMulti;
        projectile.getPersistentDataContainer().set(critKey, PersistentDataType.INTEGER, 1);
      }
      projectile.getPersistentDataContainer().set(dmgKey, PersistentDataType.DOUBLE, dmg);
    } else {
      projectile.getPersistentDataContainer().set(dmgKey, PersistentDataType.DOUBLE, 10D);
    }
  }

  @EventHandler
  public void onCombat(final EntityDamageByEntityEvent event) {
    Entity attacker = event.getDamager();
    final Entity defender = event.getEntity();

    double damage = 0D;
    boolean crit = false;
    boolean isRanged = false;

    if (attacker instanceof Projectile) {
      isRanged = true;
      final Projectile projectile = ((Projectile) attacker);
      final Integer critInt = projectile.getPersistentDataContainer().get(critKey, PersistentDataType.INTEGER);
      crit = critInt != null;
      damage = projectile.getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
      final ProjectileSource source = projectile.getShooter();
      if (source instanceof Entity) {
        attacker = (Entity) source;
      }
    }

    if (!(attacker instanceof LivingEntity && defender instanceof LivingEntity)) {
      return;
    }

    final LivingEntity defenderLiving = (LivingEntity) defender;
    final LivingEntity attackerLiving = (LivingEntity) attacker;
    final CombatStatHolder holderAttacker = combatStatManager.getCombatStatHolder(attackerLiving);
    final CombatStatHolder holderDefender = combatStatManager.getCombatStatHolder(defenderLiving);
    final boolean isPlayerAttacker = attacker instanceof Player;

    event.setDamage(0);

    if (!isRanged) {
      damage = holderAttacker.getStatValue(isRanged ? CombatStat.RANGE_DAMAGE : CombatStat.MEELE_DAMAGE);

      crit = holderAttacker.getStatValue(CombatStat.CRIT_CHANCE) >= critChanceRandom
          .nextDouble(0, 100);

      if (crit) {
        final double dmgMulti = (1D / 100D) * holderAttacker.getStatValue(CombatStat.CRIT_DAMAGE);
        damage *= dmgMulti;
      }
    }

    damage = DamageEvaluator.calculateDamage(holderDefender, damage, CombatDamageType.ofBukkit(event.getCause()));

    ItemStack attackItem = attackerLiving.getActiveItem();
    if (isPlayerAttacker) {
      final float multi = UtilPlayer.getAttackCooldown((Player) attacker);
      if (multi < 0.9F) {
        damage *= 0.05;
      } else {
        damage *= multi;
      }
      attackItem = ((Player) attacker).getInventory().getItemInMainHand();
    }
    if (attackItem != null && attackItem.getType() != Material.AIR) {
      final StatItem statItem = StatItem.of(attackItem);
      final WeaponType weaponType = statItem.getWeaponType();
      if (!isRanged && weaponType != null && !weaponType.isMeele()) {
        System.out.println("Blocked");
        event.setCancelled(true);
        return;
      }
      if (statItem.isItemStatComponent()) {
        final Integer durability = statItem.getDurability();
        if (durability != null) {
          Preconditions.checkState(durability >= 0, "Item durability is below 0");
          if (durability == 0) {
            damage *= 0.025;
          } else if (durability > 0) {
            final int leftDurability = durability - 1;
            if (leftDurability == 0) {
              // TODO effect for breaking tool
            }
            statItem.setDurability(leftDurability);
            if (isPlayerAttacker) {
              ((Player) attacker).getInventory().setItemInMainHand(statItem.getItemStack());
            } else {
              attackerLiving.getEquipment().setItemInMainHand(statItem.getItemStack());
            }
          }
        }
      }
    }

    damage = UtilMath.cut(damage, 1);

    if (isPlayerAttacker) {
      final Player attackerPlayer = (Player) attacker;
      createDamageHologram(attackerPlayer, defender, crit, damage);
    }

    double healthLeft = defenderLiving.getHealth() - damage;
    if (healthLeft <= 0) {
      healthLeft = 0;
    }
    defenderLiving.setHealth(healthLeft);

    final List<Entity> pass = defenderLiving.getPassengers();
    if (!pass.isEmpty()) {
      final Entity token = pass.get(0);
      if (token.getType() == EntityType.ARMOR_STAND) {
        final AbstractInfoBar bar = infoBarManager.getInfoBar(pass.get(0));
        if (bar == null) {
          return;
        }
        bar.setLine(0, "" + UtilMath.getHPBarAsChar(defenderLiving.getHealth(), holderDefender.getStatValue(CombatStat.HEALTH)));
      }
    }

  }

  private void createDamageHologram(final Player attackerPlayer, final Entity def,
      final boolean crit, final double dmg) {
    final String holoMsg = (crit ? "§c" : "§e") + dmg;
    final Location defLoc = def.getLocation().clone().add(0, 0.5, 0);
    final Vector directionAdjustVec = attackerPlayer.getLocation().getDirection().clone()
        .multiply(BASE_SCALAR_XZ).normalize().multiply(0.05);
    final Vector holoVel = BASE_HOLOGRAM_VELOCITY.clone().add(directionAdjustVec);
    final MovingHologram moving = hologramManager
        .createMovingHologram(defLoc, holoVel, HOLOGRAM_LIFE_TICKS);
    moving.getHologram().appendTextLine(holoMsg);
  }

  private void createHealingHologram(final LivingEntity livingDefender, final double heal) {
    final String holoMsg = "§a+" + heal;
    final Location defLoc = livingDefender.getLocation().clone().add(0, 0.5, 0);
    final Vector holoVel = BASE_HOLOGRAM_VELOCITY.clone().multiply(1.2);
    final MovingHologram moving = hologramManager
        .createMovingHologram(defLoc, holoVel, HOLOGRAM_LIFE_TICKS);
    moving.getHologram().appendTextLine(holoMsg);
  }

}
