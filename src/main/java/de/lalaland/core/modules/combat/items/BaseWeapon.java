package de.lalaland.core.modules.combat.items;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.resourcepack.skins.Model;
import de.lalaland.core.utils.items.ItemBuilder;
import de.lalaland.core.utils.nbtapi.NBTItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
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
public enum BaseWeapon implements BaseItemProvider {

  WOODEN_SWORD("Holzschwert", Model.RED_X, 1, 5.0, 200D, 200, WeaponType.SWORD),
  WOODEN_DAGGER("Holzdolch", Model.RED_X, 1, 3.0, 300D, 200, WeaponType.DAGGER),
  WOODEN_AXE("Holzaxt", Model.RED_X, 1, 6.0, 150D, 200, WeaponType.AXE),
  OAK_SHORT_BOW("Eichen Kurbogen", Model.RED_X, 1, 6.0, 150D, 200, WeaponType.SHORT_BOW),
  WOODEN_MACE("Holzstreitkolben", Model.RED_X, 1, 7.0, 100D, 200, WeaponType.MACE);

  @Getter
  private final String displayName;
  @Getter
  private final Model model;
  @Getter
  private final int baseLevel;
  @Getter
  private final double baseDamage;
  @Getter
  private final double baseAttackSpeed;
  @Getter
  private final int baseMaxDurability;
  @Getter
  private final WeaponType weaponType;

  @EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    final Location placeLoc = event.getBlock().getLocation();
    final ArmorStand armorStand = event.getPlayer().getWorld().spawn(placeLoc, ArmorStand.class);
    event.setCancelled(true);
  }

  @Override
  public ItemStack createBaseItem() {
    final NBTItem nbt = new NBTItem(new ItemBuilder(model.getItem())
        .name(displayName)
        .build());

    final StatItem statItem = StatItem.of(nbt);

    statItem.setWeaponType(weaponType);
    statItem.setDurability(baseMaxDurability);
    statItem.setMaxDurability(baseMaxDurability);
    statItem.setLevelRequirement(baseLevel);
    statItem.setCombatStat(CombatStat.ATTACK_SPEED, baseAttackSpeed);
    if (weaponType.isMeele()) {
      System.out.println("Adding meele");
      statItem.setCombatStat(CombatStat.MEELE_DAMAGE, baseDamage);
    } else {
      System.out.println("Adding ranged");
      statItem.setCombatStat(CombatStat.RANGE_DAMAGE, baseDamage);
    }

    statItem.addItemInfoCompiler();

    return statItem.getItemStack();
  }

}
