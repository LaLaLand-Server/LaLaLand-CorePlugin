package de.lalaland.core.modules.mobs.modeledentities.bipiped;

import com.google.common.base.Preconditions;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 07.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
public class BiModelEquipment {

  public static BiModelEquipment ofModelIDs(final int... modelIds) {
    Preconditions.checkArgument(modelIds.length == 10, "Needs exactly 10 Items.");
    final ItemStack head = new ItemBuilder(Material.STICK).modelData(modelIds[0]).build();
    final ItemStack left = new ItemBuilder(Material.STICK).modelData(modelIds[1]).build();
    final ItemStack right = new ItemBuilder(Material.STICK).modelData(modelIds[2]).build();
    final ItemStack torso = new ItemBuilder(Material.STICK).modelData(modelIds[3]).build();
    final ItemStack torso_moving = new ItemBuilder(Material.STICK).modelData(modelIds[4]).build();
    final ItemStack head_hurt = new ItemBuilder(Material.STICK).modelData(modelIds[5]).build();
    final ItemStack left_hurt = new ItemBuilder(Material.STICK).modelData(modelIds[6]).build();
    final ItemStack right_hurt = new ItemBuilder(Material.STICK).modelData(modelIds[7]).build();
    final ItemStack torso_hurt = new ItemBuilder(Material.STICK).modelData(modelIds[8]).build();
    final ItemStack torso_moving_hurt = new ItemBuilder(Material.STICK).modelData(modelIds[9]).build();
    return new BiModelEquipment(head, left, right, torso, torso_moving, head_hurt, left_hurt, right_hurt, torso_hurt, torso_moving_hurt);
  }

  private final ItemStack headItem;
  private final ItemStack leftHand;
  private final ItemStack rightHand;
  private final ItemStack torso;
  private final ItemStack torso_moving;
  private final ItemStack headItem_hurt;
  private final ItemStack leftHand_hurt;
  private final ItemStack rightHand_hurt;
  private final ItemStack torso_hurt;
  private final ItemStack torso_moving_hurt;

}
