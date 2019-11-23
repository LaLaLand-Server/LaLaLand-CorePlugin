package de.lalaland.core.modules.recipe;

import org.bukkit.inventory.ItemStack;

public interface IRecipe {

  int getRequiredLevel();

  ItemStack getResultItem();

  double getIngameXp();

  long getExp();

}
