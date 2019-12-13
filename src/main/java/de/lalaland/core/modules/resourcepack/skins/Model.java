package de.lalaland.core.modules.resourcepack.skins;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.lalaland.core.modules.resourcepack.packing.BoxedFontChar;
import de.lalaland.core.utils.items.ItemBuilder;
import java.io.File;
import java.lang.reflect.Field;
import lombok.Getter;
import lombok.Setter;
import net.crytec.libs.protocol.skinclient.data.Skin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum Model {

  BLACK_ARROW_UP(Material.STICK, 1000, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  BLACK_ARROW_DOWN(Material.STICK, 1001, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  BLACK_ARROW_LEFT(Material.STICK, 1002, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  BLACK_ARROW_RIGHT(Material.STICK, 1003, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  GREEN_CHECK(Material.STICK, 1004, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  RED_X(Material.STICK, 1005, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false),
  SERVER_ICON(Material.STICK, 1006, ModelData.common(), FontMeta.of(32, 32, "bitmap"), new BoxedFontChar(), false),
  HP_100(Material.STICK, 2000, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_95(Material.STICK, 2001, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_90(Material.STICK, 2002, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_85(Material.STICK, 2003, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_80(Material.STICK, 2004, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_75(Material.STICK, 2005, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_70(Material.STICK, 2006, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_65(Material.STICK, 2007, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_60(Material.STICK, 2008, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_55(Material.STICK, 2009, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_50(Material.STICK, 2010, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_45(Material.STICK, 2011, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_40(Material.STICK, 2012, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_35(Material.STICK, 2013, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_30(Material.STICK, 2014, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_25(Material.STICK, 2015, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_20(Material.STICK, 2016, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_15(Material.STICK, 2017, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_10(Material.STICK, 2018, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_5(Material.STICK, 2019, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  HP_0(Material.STICK, 2020, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_100(Material.STICK, 2021, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_95(Material.STICK, 2022, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_90(Material.STICK, 2023, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_85(Material.STICK, 2024, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_80(Material.STICK, 2025, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_75(Material.STICK, 2026, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_70(Material.STICK, 2027, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_65(Material.STICK, 2028, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_60(Material.STICK, 2029, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_55(Material.STICK, 2030, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_50(Material.STICK, 2031, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_45(Material.STICK, 2032, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_40(Material.STICK, 2033, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_35(Material.STICK, 2034, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_30(Material.STICK, 2035, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_25(Material.STICK, 2036, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_20(Material.STICK, 2037, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_15(Material.STICK, 2038, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_10(Material.STICK, 2039, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_5(Material.STICK, 2040, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  PROGRESS_0(Material.STICK, 2041, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false),
  AC_SEMIDIRT(Material.STICK, 9000, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true),
  BIT_8_GRAY(Material.STICK, 9001, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true),
  BIT_8_COBBLE(Material.STICK, 9002, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true);

  Model(final Material baseMaterial, final int modelID, final ModelData modelData, final FontMeta fontMeta,
      final BoxedFontChar boxedFontChar, final boolean headEnabled) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = modelData;
    this.fontMeta = fontMeta;
    this.boxedFontChar = boxedFontChar;
    headSkinEnabled = headEnabled;
  }

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
  @Getter
  private final boolean headSkinEnabled;
  @Getter
  @Setter
  private Skin skin;
  @Getter
  @Setter
  private File linkedImageFile;
  @Getter
  private GameProfile gameProfile;

  private ItemStack head;

  private ItemStack item;

  private void initProfile() {
    if (gameProfile == null && skin != null) {
      gameProfile = new GameProfile(skin.data.uuid, skin.name);
      gameProfile.getProperties().put("textures", new Property("textures", skin.data.texture.value, skin.data.texture.signature));
    }
  }

  public char getChar() {
    return boxedFontChar.getAsCharacter();
  }

  public ItemStack getItem() {
    if (item == null) {
      item = new ItemBuilder(baseMaterial).modelData(modelID).build();
    }
    return item.clone();
  }

  public ItemStack getHead() {
    if (head != null) {
      return head.clone();
    }
    initProfile();
    final ItemStack newHead = new ItemStack(Material.PLAYER_HEAD);
    final SkullMeta headMeta = (SkullMeta) newHead.getItemMeta();
    Field profileField = null;

    try {
      profileField = headMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(headMeta, gameProfile);
    } catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
      e1.printStackTrace();
    }

    newHead.setItemMeta(headMeta);
    head = newHead;
    return head.clone();
  }

}