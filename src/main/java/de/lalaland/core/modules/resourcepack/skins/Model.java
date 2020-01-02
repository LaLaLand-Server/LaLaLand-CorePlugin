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

  BLACK_ARROW_UP(Material.STICK, 1000, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BLACK_ARROW_DOWN(Material.STICK, 1001, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BLACK_ARROW_LEFT(Material.STICK, 1002, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BLACK_ARROW_RIGHT(Material.STICK, 1003, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  GREEN_CHECK(Material.STICK, 1004, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  RED_X(Material.STICK, 1005, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  SERVER_ICON(Material.STICK, 1006, ModelData.common(), FontMeta.of(32, 32, "bitmap"), new BoxedFontChar(), false, false),
  ATTACK_SPEED_ICON(Material.STICK, 1007, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BIO_ARMOR_ICON(Material.STICK, 1008, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  CRIT_CHANCE_ICON(Material.STICK, 1009, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  CRIT_DAMAGE_ICON(Material.STICK, 1010, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  HEALTH_ICON(Material.STICK, 1011, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  MAGIC_ARMOR_ICON(Material.STICK, 1012, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  MEELE_DAMAGE_ICON(Material.STICK, 1013, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  MIGHT_ICON(Material.STICK, 1014, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  PHYSICAL_ARMOR_ICON(Material.STICK, 1015, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  RANGED_DAMAGE_ICON(Material.STICK, 1017, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  SPEED_ICON(Material.STICK, 1018, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  MANA_ICON(Material.STICK, 1019, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  COINPILE_TINY(Material.STICK, 1100, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_SMALL(Material.STICK, 1101, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_MEDIUM(Material.STICK, 1102, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_BIG(Material.STICK, 1103, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_HUGE(Material.STICK, 1104, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_BAR_SMALL(Material.STICK, 1105, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_BAR_MEDIUM(Material.STICK, 1106, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  COINPILE_BAR_BIG(Material.STICK, 1107, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, true),
  JOB_ICON_WOODCUTTER(Material.STICK, 1200, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_MINING(Material.STICK, 1201, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_FISHING(Material.STICK, 1202, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_HERBLORE(Material.STICK, 1203, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_BREWING(Material.STICK, 1204, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_CONSTRUCTION(Material.STICK, 1205, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_COOKING(Material.STICK, 1206, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_CRAFTING(Material.STICK, 1207, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_JEWEL_CRAFTING(Material.STICK, 1208, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  JOB_ICON_SMITHING(Material.STICK, 1209, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  OAK_LOGS(Material.STICK, 1250, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  OAK_ROOT(Material.STICK, 1251, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  RESIN(Material.STICK, 1252, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BIRCH_LOGS(Material.STICK, 1253, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BIRCH_ROOT(Material.STICK, 1254, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  BIRCH_BARK(Material.STICK, 1255, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  SPRUCE_LOGS(Material.STICK, 1256, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  SPRUCE_ROOT(Material.STICK, 1257, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  SYRUP(Material.STICK, 1258, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  COPPER_ORE_ITEM(Material.STICK, 1300, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  TIN_ORE_ITEM(Material.STICK, 1301, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  IRON_ORE_ITEM(Material.STICK, 1302, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  STILLIT_ORE_ITEM(Material.STICK, 1303, ModelData.common(), FontMeta.common(), new BoxedFontChar(), false, false),
  HP_100(Material.STICK, 2000, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_95(Material.STICK, 2001, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_90(Material.STICK, 2002, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_85(Material.STICK, 2003, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_80(Material.STICK, 2004, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_75(Material.STICK, 2005, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_70(Material.STICK, 2006, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_65(Material.STICK, 2007, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_60(Material.STICK, 2008, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_55(Material.STICK, 2009, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_50(Material.STICK, 2010, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_45(Material.STICK, 2011, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_40(Material.STICK, 2012, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_35(Material.STICK, 2013, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_30(Material.STICK, 2014, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_25(Material.STICK, 2015, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_20(Material.STICK, 2016, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_15(Material.STICK, 2017, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_10(Material.STICK, 2018, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_5(Material.STICK, 2019, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  HP_0(Material.STICK, 2020, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_100(Material.STICK, 2021, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_95(Material.STICK, 2022, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_90(Material.STICK, 2023, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_85(Material.STICK, 2024, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_80(Material.STICK, 2025, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_75(Material.STICK, 2026, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_70(Material.STICK, 2027, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_65(Material.STICK, 2028, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_60(Material.STICK, 2029, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_55(Material.STICK, 2030, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_50(Material.STICK, 2031, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_45(Material.STICK, 2032, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_40(Material.STICK, 2033, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_35(Material.STICK, 2034, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_30(Material.STICK, 2035, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_25(Material.STICK, 2036, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_20(Material.STICK, 2037, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_15(Material.STICK, 2038, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_10(Material.STICK, 2039, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_5(Material.STICK, 2040, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  PROGRESS_0(Material.STICK, 2041, ModelData.common(), FontMeta.of(8, 8, "bitmap"), new BoxedFontChar(), false, false),
  AC_SEMIDIRT(Material.STICK, 9000, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true, false),
  BIT_8_GRAY(Material.STICK, 9001, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true, false),
  BIT_8_COBBLE(Material.STICK, 9002, ModelData.common(), FontMeta.common(), new BoxedFontChar(), true, false);

  Model(final Material baseMaterial, final int modelID, final ModelData modelData, final FontMeta fontMeta,
      final BoxedFontChar boxedFontChar, final boolean headEnabled, final boolean customModelDataEnabled) {
    this.baseMaterial = baseMaterial;
    this.modelID = modelID;
    this.modelData = modelData;
    this.fontMeta = fontMeta;
    this.boxedFontChar = boxedFontChar;
    headSkinEnabled = headEnabled;
    this.customModelDataEnabled = customModelDataEnabled;
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
  private final boolean customModelDataEnabled;
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