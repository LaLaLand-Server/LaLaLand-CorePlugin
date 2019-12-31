package de.lalaland.core.modules.jobs.jobdata;

import com.google.common.collect.Lists;
import de.lalaland.core.modules.resourcepack.skins.Model;
import de.lalaland.core.utils.items.ItemBuilder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 25.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
// TODO descriptions
@AllArgsConstructor
public enum JobType {

  MINING("Bergbau", Model.JOB_ICON_MINING,
      Lists.newArrayList("", "")),
  WOODCUTTER("Holzfällerei", Model.JOB_ICON_WOODCUTTER,
      Lists.newArrayList("", "")),
  HERBLORE("Wildhüterei", Model.JOB_ICON_HERBLORE,
      Lists.newArrayList("", "")), // Pflanzenkunde?
  FISHING("Fischen", Model.JOB_ICON_FISHING,
      Lists.newArrayList("", "")),

  CONSTRUCTION("Konstruktion", Model.JOB_ICON_CONSTRUCTION,
      Lists.newArrayList("", "")),
  SMITHING("Schmieden", Model.JOB_ICON_SMITHING,
      Lists.newArrayList("", "")),
  BREWING("Alchemie", Model.JOB_ICON_BREWING,
      Lists.newArrayList("", "")),
  CRAFTING("Handwerk", Model.JOB_ICON_CRAFTING,
      Lists.newArrayList("", "")),
  JEWEL_CRAFTING("Schmuck Handwerk", Model.JOB_ICON_JEWEL_CRAFTING,
      Lists.newArrayList("", "")),
  COOKING("Kochen", Model.JOB_ICON_COOKING,
      Lists.newArrayList("", ""));

  @Getter
  private final String displayName;
  @Getter
  private final Model model;
  @Getter
  private final List<String> description;

  public ItemStack getIcon(final JobHolder jobHolder) {
    final ItemBuilder itemBuilder = new ItemBuilder(model.getItem());
    itemBuilder.name("§6" + displayName + " §e[§f" + jobHolder.getJobData(this).getLevel() + "§e]");
    itemBuilder.lore("");
    itemBuilder.lore(this.description);
    return itemBuilder.build();
  }

}