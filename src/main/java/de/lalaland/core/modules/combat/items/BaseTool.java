package de.lalaland.core.modules.combat.items;

import de.lalaland.core.modules.jobs.jobdata.JobType;
import de.lalaland.core.modules.resourcepack.skins.Model;
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

  WOODEN_PICKAXE("Holz Spitzhacke", JobType.MINING, 5D, 1, 200, Model.RED_X),
  WOODEN_WOODCUTTING_AXE("Holz Axt", JobType.WOODCUTTER, 5D, 1, 200, Model.RED_X),
  WOODEN_SPADE("Holz Spaten", JobType.HERBLORE, 5D, 1, 200, Model.RED_X);

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
  private final Model model;

  @Override
  public ItemStack createBaseItem() {
    final StatItem statItem = StatItem.of(new ItemBuilder(model.getItem()).name(displayName).build());

    statItem.setDurability(baseMaxDurability);
    statItem.setMaxDurability(baseMaxDurability);
    statItem.setJobRequirement(linkedJobType, baseJobReqLevel);
    statItem.setJobValue(linkedJobType, baseJobValue);
    statItem.addItemInfoCompiler();

    return statItem.getItemStack();
  }

}
