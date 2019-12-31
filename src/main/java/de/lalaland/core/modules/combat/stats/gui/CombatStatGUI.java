package de.lalaland.core.modules.combat.stats.gui;

import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.modules.combat.stats.CombatStatHolder;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;

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

    content.set(SlotPos.of(1, 1), ClickableItem.of(CombatStat.HEALTH.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(1, 2), ClickableItem.of(CombatStat.MANA.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(1, 3), ClickableItem.of(CombatStat.SPEED.getIcon(holder),  (event) ->{

    }));

    content.set(SlotPos.of(1, 5), ClickableItem.of(CombatStat.BIO_ARMOR.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(1, 6), ClickableItem.of(CombatStat.PHYSICAL_ARMOR.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(1, 7), ClickableItem.of(CombatStat.MYSTIC_ARMOR.getIcon(holder),  (event) ->{

    }));

    content.set(SlotPos.of(2, 1), ClickableItem.of(CombatStat.RANGE_DAMAGE.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(2, 2), ClickableItem.of(CombatStat.MEELE_DAMAGE.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(2, 3), ClickableItem.of(CombatStat.MIGHT.getIcon(holder),  (event) ->{

    }));

    content.set(SlotPos.of(2, 5), ClickableItem.of(CombatStat.ATTACK_SPEED.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(2, 6), ClickableItem.of(CombatStat.CRIT_CHANCE.getIcon(holder),  (event) ->{

    }));
    content.set(SlotPos.of(2, 7), ClickableItem.of(CombatStat.CRIT_DAMAGE.getIcon(holder),  (event) ->{

    }));

  }

}