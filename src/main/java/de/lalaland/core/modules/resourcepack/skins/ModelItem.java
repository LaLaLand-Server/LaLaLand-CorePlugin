package de.lalaland.core.modules.resourcepack.skins;

import de.lalaland.core.modules.resourcepack.packing.BoxedFontChar;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

  BLACK_ARROW_UP(Material.STICK, 1000, ModelData.common(), FontMeta.common(), new BoxedFontChar()),
  BLACK_ARROW_DOWN(Material.STICK, 1001, ModelData.common(), FontMeta.common(), new BoxedFontChar()),
  BLACK_ARROW_LEFT(Material.STICK, 1002, ModelData.common(), FontMeta.common(), new BoxedFontChar()),
  BLACK_ARROW_RIGHT(Material.STICK, 1003, ModelData.common(), FontMeta.common(), new BoxedFontChar()),
  GREEN_CHECK(Material.STICK, 1004, ModelData.common(), FontMeta.common(), new BoxedFontChar()),
  RED_X(Material.STICK, 1005, ModelData.common(), FontMeta.common(), new BoxedFontChar());

  @Getter
  private final Material baseMaterial;
  @Getter
  private final int modelID;
  @Getter
  private final ModelData modelData;
  @Getter
  private final FontMeta fontMeta;
  @Getter
  private final BoxedFontChar boxedFontChar;

  public char getChar() {
    return boxedFontChar.getAsCharacter();
  }

  public ItemStack create() {
    return new ItemBuilder(baseMaterial).modelData(modelID).build();
  }

}
