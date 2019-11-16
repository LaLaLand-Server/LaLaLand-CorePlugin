package de.lalaland.core.modules.CombatModule.weapons;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.lalaland.core.modules.CombatModule.stats.CombatStat;
import de.lalaland.core.utils.nbtapi.NBTCompound;
import de.lalaland.core.utils.nbtapi.NBTItem;
import java.util.Map;
import javax.annotation.Nullable;
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
public class StatItem {

  public static StatItem of(ItemStack item) {
    Preconditions.checkNotNull(item, "ItemStack can not be null.");
    return of(new NBTItem(item));
  }

  public static StatItem of(NBTItem nbtItem) {
    return new StatItem(nbtItem);
  }

  /**
   * This class is used to fetch stats from any ItemStack.
   *
   * @param nbt the item as NBTItem
   */
  private StatItem(NBTItem nbt) {
    this.nbt = nbt;
    this.hasCombatStats = nbt.hasKey(CombatStat.COMPOUND_KEY);
    this.hasItemStats = nbt.hasKey(ItemStat.COMPOUND_KEY);
  }

  private final NBTItem nbt;
  private final boolean hasCombatStats;
  private final boolean hasItemStats;

  /**
   * Fetches a full mapping of combat stats.
   *
   * @return a map or null if no stats are present.
   */
  @Nullable
  public Map<CombatStat, Double> getCombatStatMap() {
    Map<CombatStat, Double> map = null;

    if (this.hasCombatStats) {
      map = Maps.newHashMap();
      NBTCompound statComp = nbt.getCompound(CombatStat.COMPOUND_KEY);
      for (String key : statComp.getKeys()) {
        map.put(CombatStat.valueOf(key), statComp.getDouble(key));
      }
    }

    return map;
  }

  /**
   * Fetches the value of one specific stat.
   * @param stat
   * @return the value or 0
   */
  public double getCombatStatValue(CombatStat stat) {
    if (this.hasCombatStats) {
      NBTCompound statComp = nbt.getCompound(CombatStat.COMPOUND_KEY);
      if (statComp.hasKey(stat.toString())) {
        return statComp.getDouble(stat.toString());
      }
    }

    return 0D;
  }

  /**
   * Fetches the WeponType of this item.
   * @return the type or null
   */
  @Nullable
  public WeaponType getWeaponType() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.WEAPON_TYPE.getNbtKey())) {
        return WeaponType
            .valueOf(itemStatCompound.getString(ItemStat.WEAPON_TYPE.getNbtKey()));
      }
    }
    return null;
  }

  /**
   * Fetches the MaxDurability of this item.
   * @return the value or null
   */
  @Nullable
  public Integer getMaxDurability() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.MAX_DURABILITY.getNbtKey())) {
        return itemStatCompound.getInteger(ItemStat.MAX_DURABILITY.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Durability of this item.
   * @return the value or null
   */
  @Nullable
  public Integer getDurability() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.DURABILITY.getNbtKey())) {
        return itemStatCompound.getInteger(ItemStat.DURABILITY.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Version of this item.
   * @return the version or null
   */
  @Nullable
  public String getVersion() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.VERSION.getNbtKey())) {
        return itemStatCompound.getString(ItemStat.VERSION.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the CreationDate of this item as Unix long.
   * @return the unix time or null
   */
  @Nullable
  public Long getCreationDate() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.CREATION_DATE.getNbtKey())) {
        return itemStatCompound.getLong(ItemStat.CREATION_DATE.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Unbreakable state of this item.
   * @return if unbreakable. Default is true.
   */
  @Nullable
  public Boolean getUnbreakable() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.UNBREAKABLE.getNbtKey())) {
        return itemStatCompound.getBoolean(ItemStat.UNBREAKABLE.getNbtKey());
      }
    }
    return true;
  }

  /**
   * Fetches the ItemCreator as String.
   * Might be some players UUID or SERVER
   *
   * @return the unix time or null
   */
  @Nullable
  public String getItemCreator() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.CREATOR.getNbtKey())) {
        return itemStatCompound.getString(ItemStat.CREATOR.getNbtKey());
      }
    }
    return null;
  }

}
