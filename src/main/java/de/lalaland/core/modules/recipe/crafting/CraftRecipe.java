package de.lalaland.core.modules.recipe.crafting;

import de.lalaland.core.modules.recipe.IRecipe;
import de.lalaland.core.modules.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public abstract class CraftRecipe extends Recipe implements IRecipe {

  private final ItemStack[] items;

}
