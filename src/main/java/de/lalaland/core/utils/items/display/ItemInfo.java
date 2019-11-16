package de.lalaland.core.utils.items.display;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemInfo {

  protected ItemInfo(ItemStack inputItem) {
    this.inputClone = inputItem.clone();
    meta = inputItem.getItemMeta();
    if (meta.hasDisplayName()) {
      this.displayName = meta.getDisplayName();
    }
    if (meta.hasLore()) {
      this.lores = meta.getLore();
    }
  }

  private final ItemStack inputClone;
  private final ItemMeta meta;
  private String displayName;
  private List<String> lores;

  public void setLore(List<String> lines) {
    this.lores.clear();
    for (String line : lines) {
      this.lores.add(line);
    }
  }

  public void setLore(String... lines) {
    this.lores.clear();
    for (String line : lines) {
      this.lores.add(line);
    }
  }

  public void addLore(String loreLine) {
    this.lores.add(loreLine);
  }

  public void setName(String name) {
    this.displayName = name;
  }

  protected ItemStack getResult() {
    this.meta.setDisplayName(this.displayName);
    this.meta.setLore(this.lores);
    this.inputClone.setItemMeta(this.meta);
    return this.inputClone;
  }

}
