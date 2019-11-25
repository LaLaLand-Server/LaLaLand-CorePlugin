package de.lalaland.core.modules.combat.items;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import de.lalaland.core.utils.items.ItemBuilder;
import de.lalaland.core.utils.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum BaseWeapon {

  WOODEN_SWORD("Holzschwert", ModelItem.RED_X, 5.0, 200D, 200),
  WOODEN_DAGGER("Holzdolch", ModelItem.RED_X, 3.0, 300D, 200),
  WOODEN_AXE("Holzaxt", ModelItem.RED_X, 6.0, 150D, 200),
  WOODEN_MACE("Holzstreitkolben", ModelItem.RED_X, 7.0, 100D, 200);

  @Getter
  private final String displayName;
  @Getter
  private final ModelItem model;
  @Getter
  private final double baseDamage;
  @Getter
  private final double baseAttackSpeed;
  @Getter
  private final int baseMaxDurability;

  public ItemStack createBaseItem() {
    final NBTItem nbt = new NBTItem(new ItemBuilder(model.create())
        .name(displayName)
        .build());

    final StatItem statItem = StatItem.of(nbt);

    statItem.setDurability(baseMaxDurability);
    statItem.setMaxDurability(baseMaxDurability);
    statItem.setCombatStat(CombatStat.ATTACK_SPEED, baseAttackSpeed);
    statItem.setCombatStat(CombatStat.MEELE_DAMAGE, baseDamage);
    statItem.addItemInfoCompiler();

    return statItem.getItemStack();
  }

}
