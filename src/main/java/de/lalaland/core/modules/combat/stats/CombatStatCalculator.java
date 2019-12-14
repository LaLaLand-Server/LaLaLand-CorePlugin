package de.lalaland.core.modules.combat.stats;

import de.lalaland.core.modules.combat.items.StatItem;
import java.util.Map;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatStatCalculator {

  /**
   * Recalculates the item values of any {@link CombatStatHolder}.
   *
   * @param holder
   */
  protected void recalculateValues(final CombatStatHolder holder) {
    final LivingEntity entity = holder.getBukkitEntity();
    final double preHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    if (entity instanceof Player) {
      calculateForPlayer((Player) entity, holder);
    } else {
      calculateForEntity(entity, holder);
    }
    final double postHealth = holder.getStatValue(CombatStat.HEALTH);
    if (preHealth != postHealth) {
      entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(postHealth);
    }
    if (entity instanceof Player) {
      final float walk = (float) (0.002D * holder.getStatValue(CombatStat.SPEED));
      ((Player) entity).setWalkSpeed(walk);
      final double atsp = 0.005D * holder.getStatValue(CombatStat.ATTACK_SPEED);
      entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(atsp);
    }
  }

  /**
   * Calculation for human entities.
   *
   * @param player
   * @param holder
   */
  private void calculateForPlayer(final Player player, final CombatStatHolder holder) {
    final PlayerInventory inventory = player.getInventory();
    final Map<CombatStat, Double> statMap = CombatStat.getEmptyMap();
    final ItemStack[] validStatItems = new ItemStack[6];
    validStatItems[4] = inventory.getItemInMainHand();
    validStatItems[5] = inventory.getItemInOffHand();
    final ItemStack[] armor = inventory.getArmorContents();

    for (int index = 0; index < 4; index++) {
      validStatItems[index] = armor[index];
    }

    for (final ItemStack item : validStatItems) {
      if (item == null) {
        continue;
      }
      mergeStats(StatItem.of(item), statMap);
    }

    statMap.forEach((stat, value) -> holder.setExtraValue(stat, value));
  }

  /**
   * Calculation for random entities.
   *
   * @param entity
   * @param holder
   */
  private void calculateForEntity(final LivingEntity entity, final CombatStatHolder holder) {

    final Map<CombatStat, Double> statMap = CombatStat.getEmptyMap();
    final EntityEquipment equipment = entity.getEquipment();

    if (equipment == null) {
      return;
    }
    final ItemStack[] validStatItems = new ItemStack[6];
    validStatItems[4] = equipment.getItemInMainHand();
    validStatItems[5] = equipment.getItemInOffHand();
    final ItemStack[] armor = equipment.getArmorContents();
    for (int index = 0; index < 4; index++) {
      validStatItems[index] = armor[index];
    }
    for (final ItemStack item : validStatItems) {
      if (item == null) {
        continue;
      }
      mergeStats(StatItem.of(item), statMap);
    }

    statMap.forEach((stat, value) -> holder.setExtraValue(stat, value));
  }

  /**
   * Util method for merging.
   *
   * @param statItem
   * @param valueMap
   */
  private void mergeStats(final StatItem statItem, final Map<CombatStat, Double> valueMap) {
    final Map<CombatStat, Double> itemMap = statItem.getCombatStatMap();
    if (itemMap == null) {
      return;
    }
    itemMap.forEach((stat, value) -> valueMap.merge(stat, value, Double::sum));
  }

}
