package de.lalaland.core.utils.nbtapi;

import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 27.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class NBTItem extends NBTCompound {

  public NBTItem(final org.bukkit.inventory.ItemStack bukkitItem) {
    this(CraftItemStack.asNMSCopy(bukkitItem));
  }

  public NBTItem(final ItemStack nmsItem) {
    this(nmsItem, nmsItem.getOrCreateTag());
  }

  private NBTItem(final ItemStack nmsItem, final NBTTagCompound mainCompound) {
    super(mainCompound);
    this.nmsItem = nmsItem;
    this.mainCompound = new NBTCompound(mainCompound);
  }

  private final ItemStack nmsItem;
  private final NBTCompound mainCompound;

  public org.bukkit.inventory.ItemStack asBukkitItem() {
    return CraftItemStack.asBukkitCopy(nmsItem);
  }

  public org.bukkit.inventory.ItemStack getItem() {
    return asBukkitItem();
  }

}