package de.lalaland.core.modules.jobs.guis;

import de.lalaland.core.modules.jobs.jobdata.JobHolder;
import de.lalaland.core.modules.jobs.jobdata.JobType;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 31.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobGUI implements InventoryProvider {

  public static SmartInventory create(final JobHolder jobHolder) {
    return SmartInventory.builder().size(5).title("Berufe").provider(new JobGUI(jobHolder)).build();
  }

  private JobGUI(final JobHolder jobHolder) {
    this.jobHolder = jobHolder;
  }

  private final JobHolder jobHolder;

  @Override
  public void init(final Player player, final InventoryContent content) {

    content.set(SlotPos.of(1, 2), ClickableItem.of(JobType.WOODCUTTER.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(1, 3), ClickableItem.of(JobType.MINING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(1, 4), ClickableItem.of(JobType.FISHING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(1, 5), ClickableItem.of(JobType.HERBLORE.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(1, 6), ClickableItem.of(JobType.CONSTRUCTION.getIcon(jobHolder), (event) -> {

    }));

    content.set(SlotPos.of(2, 2), ClickableItem.of(JobType.BREWING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(2, 3), ClickableItem.of(JobType.COOKING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(2, 4), ClickableItem.of(JobType.CRAFTING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(2, 5), ClickableItem.of(JobType.SMITHING.getIcon(jobHolder), (event) -> {

    }));
    content.set(SlotPos.of(2, 6), ClickableItem.of(JobType.JEWEL_CRAFTING.getIcon(jobHolder), (event) -> {

    }));

  }

}