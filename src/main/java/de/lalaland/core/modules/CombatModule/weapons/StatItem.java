package de.lalaland.core.modules.CombatModule.weapons;

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
    return of(new NBTItem(item));
  }

  public static StatItem of(NBTItem nbtItem) {
    return new StatItem(nbtItem);
  }

  private StatItem(NBTItem nbt) {
    this.nbt = nbt;
    this.hasCombatStats = nbt.hasKey(CombatStat.COMPOUND_KEY);
    this.hasItemStats = nbt.hasKey(ItemStat.COMPOUND_KEY);
  }

  private final NBTItem nbt;
  private final boolean hasCombatStats;
  private final boolean hasItemStats;

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

  public double getCombatStatValue(CombatStat stat) {
    if (this.hasCombatStats) {
      NBTCompound statComp = nbt.getCompound(CombatStat.COMPOUND_KEY);
      if (statComp.hasKey(stat.toString())) {
        return statComp.getDouble(stat.toString());
      }
    }

    return 0D;
  }

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

  @Nullable
  public Boolean getUnbreakable() {
    if (this.hasItemStats) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.UNBREAKABLE.getNbtKey())) {
        return itemStatCompound.getBoolean(ItemStat.UNBREAKABLE.getNbtKey());
      }
    }
    return null;
  }

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
