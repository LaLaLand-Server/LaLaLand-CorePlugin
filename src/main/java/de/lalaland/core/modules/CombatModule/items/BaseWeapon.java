package de.lalaland.core.modules.CombatModule.items;

import de.lalaland.core.modules.CombatModule.stats.CombatStat;
import de.lalaland.core.utils.items.ItemBuilder;
import de.lalaland.core.utils.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
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

  WOODEN_SWORD("Holzschwert", Material.STICK, 100, 5.0, 200D, 200),
  WOODEN_DAGGER("Holzdolch", Material.STICK, 101, 3.0, 300D, 200),
  WOODEN_AXE("Holzaxt", Material.STICK, 102, 6.0, 150D, 200),
  WOODEN_MACE("Holzstreitkolben", Material.STICK, 103, 7.0, 100D, 200);

  @Getter
  private final String displayName;
  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final double baseDamage;
  @Getter
  private final double baseAttackSpeed;
  @Getter
  private final int baseMaxDurability;

  public ItemStack createBaseItem() {
    NBTItem nbt = new NBTItem(new ItemBuilder(this.baseMaterial)
        .name(this.displayName)
        .modelData(this.modelID)
        .build());

    StatItem statItem = StatItem.of(nbt);

    statItem.setDurability(this.baseMaxDurability);
    statItem.setCombatStat(CombatStat.ATTACK_SPEED, this.baseAttackSpeed);
    statItem.setCombatStat(CombatStat.MEELE_DAMAGE, this.baseDamage);

    return statItem.getItemStack();
  }

}
