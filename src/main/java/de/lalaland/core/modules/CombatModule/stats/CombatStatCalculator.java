package de.lalaland.core.modules.CombatModule.stats;

import com.google.common.collect.Maps;
import de.lalaland.core.modules.CombatModule.weapons.StatItem;
import java.util.Map;
import org.bukkit.EntityEffect;
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

  protected void recalculateBaseValues(CombatStatHolder holder) {
    LivingEntity entity = holder.getBukkitEntity();
    if (entity instanceof Player) {
      this.calculateForPlayer((Player) entity, holder);
    } else {
      this.calculateForEntity(entity, holder);
    }
  }

  private void calculateForPlayer(Player player, CombatStatHolder holder) {
    PlayerInventory inventory = player.getInventory();
    Map<CombatStat, Double> statMap = Maps.newHashMap();
    Map<CombatStat, Double> itemStats = null;

    for (ItemStack armorItem : inventory.getArmorContents()) {
      StatItem statItem = StatItem.of(armorItem);
      itemStats = statItem.getCombatStatMap();
      if (itemStats != null) {
        itemStats.forEach((stat, value) -> statMap.merge(stat, value, (v1, v2) -> v1 + v2));
      }
    }

    itemStats = StatItem.of(inventory.getItemInMainHand()).getCombatStatMap();
    if (itemStats != null) {
      itemStats.forEach((stat, value) -> statMap.merge(stat, value, (v1, v2) -> v1 + v2));
    }

    itemStats = StatItem.of(inventory.getItemInOffHand()).getCombatStatMap();
    if (itemStats != null) {
      itemStats.forEach((stat, value) -> statMap.merge(stat, value, (v1, v2) -> v1 + v2));
    }

    statMap.forEach((stat, value) -> holder.setValue(stat, value));
  }

  private void calculateForEntity(LivingEntity entity, CombatStatHolder holder) {
    EntityEquipment equipment = entity.getEquipment();
    if(equipment != null){

    }
  }

}
