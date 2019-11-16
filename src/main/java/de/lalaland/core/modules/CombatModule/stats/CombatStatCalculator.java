package de.lalaland.core.modules.CombatModule.stats;

import de.lalaland.core.modules.CombatModule.items.StatItem;
import java.util.Map;
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

  protected void CombatStatCalculator() {

  }

  /**
   * Recalculates the item values of
   * any {@link CombatStatHolder}.
   *
   * @param holder
   */
  protected void recalculateValues(CombatStatHolder holder) {
    LivingEntity entity = holder.getBukkitEntity();
    if (entity instanceof Player) {
      this.calculateForPlayer((Player) entity, holder);
    } else {
      this.calculateForEntity(entity, holder);
    }
  }

  /**
   * Calculation for human entities.
   *
   * @param player
   * @param holder
   */
  private void calculateForPlayer(Player player, CombatStatHolder holder) {
    PlayerInventory inventory = player.getInventory();
    Map<CombatStat, Double> statMap = CombatStat.getEmptyMap();
    ItemStack[] validStatItems = new ItemStack[6];
    validStatItems[4] = inventory.getItemInMainHand();
    validStatItems[5] = inventory.getItemInOffHand();
    ItemStack[] armor = inventory.getArmorContents();

    for (int index = 0; index < 4; index++) {
      validStatItems[index] = armor[index];
    }

    for (ItemStack item : validStatItems) {
      if (item == null) {
        continue;
      }
      mergeStats(StatItem.of(item), statMap);
    }

    statMap.forEach((stat, value) -> holder.setValue(stat, value));
  }

  /**
   * Calculation for random entities.
   *
   * @param entity
   * @param holder
   */
  private void calculateForEntity(LivingEntity entity, CombatStatHolder holder) {
    Map<CombatStat, Double> statMap = CombatStat.getEmptyMap();
    EntityEquipment equipment = entity.getEquipment();

    if (equipment == null) {
      return;
    }
    ItemStack[] validStatItems = new ItemStack[6];
    validStatItems[4] = equipment.getItemInMainHand();
    validStatItems[5] = equipment.getItemInOffHand();
    ItemStack[] armor = equipment.getArmorContents();
    for (int index = 0; index < 4; index++) {
      validStatItems[index] = armor[index];
    }
    for(ItemStack item : validStatItems){
      if(item == null) continue;
      mergeStats(StatItem.of(item), statMap);
    }

    statMap.forEach((stat, value) -> holder.setValue(stat, value));
  }

  /**
   * Util method for merging.
   *
   * @param statItem
   * @param valueMap
   */
  private void mergeStats(StatItem statItem, Map<CombatStat, Double> valueMap) {
    Map<CombatStat, Double> itemMap = statItem.getCombatStatMap();
    if (itemMap == null) {
      return;
    }
    itemMap.forEach((stat, value) -> valueMap.merge(stat, value, (v1, v2) -> v1 + v2));
  }

}
