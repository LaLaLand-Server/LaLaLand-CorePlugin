package de.lalaland.core.modules.combat.items;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.lalaland.core.modules.combat.stats.CombatStat;
import de.lalaland.core.utils.items.display.ItemDisplayCompiler;
import de.lalaland.core.utils.nbtapi.NBTCompound;
import de.lalaland.core.utils.nbtapi.NBTItem;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
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
    this.combatStatComponent = nbt.hasKey(CombatStat.COMPOUND_KEY);
    this.itemStatComponent = nbt.hasKey(ItemStat.COMPOUND_KEY);
  }

  private final NBTItem nbt;
  @Getter
  private boolean combatStatComponent;
  @Getter
  private boolean itemStatComponent;

  public ItemStack getItemStack() {
    return nbt.getItem();
  }

  public NBTItem getNbtItem() {
    return this.nbt;
  }

  /**
   * Fetches a full mapping of combat stats.
   *
   * @return a map or null if no stats are present.
   */
  @Nullable
  public Map<CombatStat, Double> getCombatStatMap() {
    Map<CombatStat, Double> map = null;

    if (this.combatStatComponent) {
      map = Maps.newHashMap();
      NBTCompound statComp = nbt.getCompound(CombatStat.COMPOUND_KEY);
      for (String key : statComp.getKeys()) {
        map.put(CombatStat.valueOf(key), statComp.getDouble(key));
      }
    }

    return map;
  }

  public void addItemInfoCompiler() {
    ItemDisplayCompiler.addDisplayCompileKey(ItemInfoCompiler.NBT_VALUE, this.nbt);
  }

  /**
   * Fetches the value of one specific stat.
   *
   * @param stat
   * @return the value or 0
   */
  public double getCombatStatValue(CombatStat stat) {
    if (this.combatStatComponent) {
      NBTCompound statComp = nbt.getCompound(CombatStat.COMPOUND_KEY);
      if (statComp.hasKey(stat.toString())) {
        return statComp.getDouble(stat.toString());
      }
    }

    return 0D;
  }

  /**
   * Fetches the WeponType of this item.
   *
   * @return the type or null
   */
  @Nullable
  public WeaponType getWeaponType() {
    if (this.itemStatComponent) {
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
   *
   * @return the value or null
   */
  @Nullable
  public Integer getMaxDurability() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.MAX_DURABILITY.getNbtKey())) {
        return itemStatCompound.getInteger(ItemStat.MAX_DURABILITY.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Durability of this item.
   *
   * @return the value or null
   */
  @Nullable
  public Integer getDurability() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.DURABILITY.getNbtKey())) {
        return itemStatCompound.getInteger(ItemStat.DURABILITY.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Version of this item.
   *
   * @return the version or null
   */
  @Nullable
  public String getVersion() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.VERSION.getNbtKey())) {
        return itemStatCompound.getString(ItemStat.VERSION.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the CreationDate of this item as Unix long.
   *
   * @return the unix time or null
   */
  @Nullable
  public Long getCreationDate() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.CREATION_DATE.getNbtKey())) {
        return itemStatCompound.getLong(ItemStat.CREATION_DATE.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Fetches the Unbreakable state of this item.
   *
   * @return if unbreakable. Default is true.
   */
  @Nullable
  public Boolean getUnbreakable() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.UNBREAKABLE.getNbtKey())) {
        return itemStatCompound.getBoolean(ItemStat.UNBREAKABLE.getNbtKey());
      }
    }
    return true;
  }

  /**
   * Fetches the ItemCreator as String. Might be some players UUID or SERVER
   *
   * @return the unix time or null
   */
  @Nullable
  public String getItemCreator() {
    if (this.itemStatComponent) {
      NBTCompound itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
      if (itemStatCompound.hasKey(ItemStat.CREATOR.getNbtKey())) {
        return itemStatCompound.getString(ItemStat.CREATOR.getNbtKey());
      }
    }
    return null;
  }

  /**
   * Sets a combat stat on this item. If the CombatStat NBTCompound is not set it will create one.
   *
   * @param stat  the CombatStat
   * @param value the value
   */
  public void setCombatStat(CombatStat stat, double value) {
    NBTCompound statCompound;

    if (!this.combatStatComponent) {
      statCompound = this.nbt.addCompound(CombatStat.COMPOUND_KEY);
      this.combatStatComponent = true;
    } else {
      statCompound = nbt.getCompound(CombatStat.COMPOUND_KEY);
    }

    statCompound.setDouble(stat.toString(), value);

  }

  /**
   * Sets the WeaponType of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param type
   */
  public void setWeaponType(WeaponType type) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setString(ItemStat.WEAPON_TYPE.getNbtKey(), type.toString());
  }

  /**
   * Sets the MaxDurability of this item. If the ItemStat NBTCompound is not set it will create
   * one.
   *
   * @param durability
   */
  public void setMaxDurability(int durability) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setInteger(ItemStat.MAX_DURABILITY.getNbtKey(), durability);
  }

  /**
   * Sets the Durability of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param durability
   */
  public void setDurability(int durability) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setInteger(ItemStat.DURABILITY.getNbtKey(), durability);
  }

  /**
   * Sets the Version of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param version
   */
  public void setVersion(String version) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setString(ItemStat.VERSION.getNbtKey(), version);
  }

  /**
   * Sets the CreationDate of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param creationDate
   */
  public void setCreationDate(long creationDate) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setLong(ItemStat.CREATION_DATE.getNbtKey(), creationDate);
  }

  /**
   * Sets the Unbreakable state of this item. If the ItemStat NBTCompound is not set it will create
   * one.
   *
   * @param state
   */
  public void setUnbreakable(boolean state) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setBoolean(ItemStat.UNBREAKABLE.getNbtKey(), state);
  }

  /**
   * Sets the Creator of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param creator
   */
  public void setCreator(String creator) {
    NBTCompound itemStatCompound;

    if (!this.itemStatComponent) {
      itemStatCompound = this.nbt.addCompound(ItemStat.COMPOUND_KEY);
      this.itemStatComponent = true;
    } else {
      itemStatCompound = this.nbt.getCompound(ItemStat.COMPOUND_KEY);
    }

    itemStatCompound.setString(ItemStat.VERSION.getNbtKey(), creator);
  }

  /**
   * Sets the Creator of this item. If the ItemStat NBTCompound is not set it will create one.
   *
   * @param creator
   */
  public void setCreator(UUID creator) {
    this.setCreator(creator.toString());
  }

}
