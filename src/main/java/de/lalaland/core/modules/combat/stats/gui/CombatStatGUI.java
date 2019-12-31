package de.lalaland.core.modules.combat.stats.gui;

import com.google.common.base.Preconditions;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import de.lalaland.core.user.User;
import de.lalaland.core.user.UserManager;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.items.ItemBuilder;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 18.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class CombatStatGUI implements InventoryProvider {

  public static SmartInventory create(CombatStatHolder holder) {
    return SmartInventory.builder().size(5).title("Stats").provider(new CombatStatGUI(holder)).build();
  }

  private CombatStatGUI(CombatStatHolder holder) {
    this.holder = holder;
  }

  private final CombatStatHolder holder;

  @Override
  public void init(Player player, InventoryContent content) {



  }

}