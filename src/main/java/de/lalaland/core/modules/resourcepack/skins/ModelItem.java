package de.lalaland.core.modules.resourcepack.skins;

import com.google.gson.JsonObject;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum ModelItem {

  BLACK_ARROW_UP(Material.STICK, 1000, "item/generated", null, null),
  BLACK_ARROW_DOWN(Material.STICK, 1001, "item/generated", null, null),
  BLACK_ARROW_LEFT(Material.STICK, 1002, "item/generated", null, null),
  BLACK_ARROW_RIGHT(Material.STICK, 1003, "item/generated", null, null);

  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final String modelParent;
  @Nullable
  @Getter
  private final JsonObject displayJson;
  @Nullable
  @Getter
  private final JsonObject elementsJson;

  public ItemStack create() {
    return new ItemBuilder(baseMaterial).modelData(modelID).build();
  }

}
