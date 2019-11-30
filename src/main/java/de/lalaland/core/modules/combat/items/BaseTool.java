package de.lalaland.core.modules.combat.items;

import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import de.lalaland.core.utils.items.ItemBuilder;
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
public enum BaseTool implements BaseItemProvider {

  WOODEN_PICKAXE("Holz Spitzhacke", JobType.MINER, 5D, 1, 200, ModelItem.RED_X),
  WOODEN_WOODCUTTING_AXE("Holz Axt", JobType.WOODCUTTER, 5D, 1, 200, ModelItem.RED_X),
  WOODEN_SPADE("Holz Spaten", JobType.GATHERER, 5D, 1, 200, ModelItem.RED_X);

  @Getter
  private final String displayName;
  @Getter
  private final JobType linkedJobType;
  @Getter
  private final double baseJobValue;
  @Getter
  private final int baseJobReqLevel;
  @Getter
  private final int baseMaxDurability;
  @Getter
  private final ModelItem model;

  @Override
  public ItemStack createBaseItem() {
    final StatItem statItem = StatItem.of(new ItemBuilder(model.create()).name(displayName).build());

    statItem.setDurability(baseMaxDurability);
    statItem.setMaxDurability(baseMaxDurability);
    statItem.setJobRequirement(linkedJobType, baseJobReqLevel);
    statItem.setJobValue(linkedJobType, baseJobValue);
    statItem.addItemInfoCompiler();

    return statItem.getItemStack();
  }

}
