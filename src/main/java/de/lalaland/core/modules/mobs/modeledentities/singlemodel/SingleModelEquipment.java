package de.lalaland.core.modules.mobs.modeledentities.singlemodel;

import com.google.common.base.Preconditions;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
public class SingleModelEquipment {

  public static SingleModelEquipment ofModelIDs(final int... modelIds) {
    Preconditions.checkArgument(modelIds.length == 5, "Needs exactly 5 Items.");
    final ItemStack idle = new ItemBuilder(Material.STRUCTURE_VOID).modelData(modelIds[0]).build();
    final ItemStack move = new ItemBuilder(Material.STRUCTURE_VOID).modelData(modelIds[1]).build();
    final ItemStack attack = new ItemBuilder(Material.STRUCTURE_VOID).modelData(modelIds[2]).build();
    final ItemStack idle_hurt = new ItemBuilder(Material.STRUCTURE_VOID).modelData(modelIds[3]).build();
    final ItemStack move_hurt = new ItemBuilder(Material.STRUCTURE_VOID).modelData(modelIds[4]).build();
    return new SingleModelEquipment(idle, move, attack, idle_hurt, move_hurt);
  }

  private final ItemStack idleItem;
  private final ItemStack moveItem;
  private final ItemStack attackItem;
  private final ItemStack idleHurtItem;
  private final ItemStack moveHurtItem;

}
