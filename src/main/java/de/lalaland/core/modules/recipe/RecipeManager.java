package de.lalaland.core.modules.recipe;

import de.lalaland.core.modules.recipe.brewing.BrewRecipe;
import de.lalaland.core.modules.recipe.crafting.CraftRecipe;
import de.lalaland.core.modules.recipe.furnace.FurnaceRecipe;
import de.lalaland.core.modules.recipe.impl.SingleInputRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class RecipeManager {

  @Getter
  private final Object2ObjectOpenHashMap<ItemStack[],CraftRecipe> craftingRecipes;
  @Getter
  private final Object2ObjectOpenHashMap<ItemStack, BrewRecipe> brewingRecipes;
  @Getter
  private final Object2ObjectOpenHashMap<ItemStack, FurnaceRecipe> furnaceRecipes;

  public RecipeManager(){
    craftingRecipes = new Object2ObjectOpenHashMap<>();
    brewingRecipes = new Object2ObjectOpenHashMap<>();
    furnaceRecipes = new Object2ObjectOpenHashMap<>();
  }

  public void registerRecipe(final IRecipe recipe){

    if(recipe instanceof CraftRecipe){

      final CraftRecipe craftRecipe = (CraftRecipe)recipe;

      craftingRecipes.put(craftRecipe.getItems(),craftRecipe);
    }

    if(recipe instanceof BrewRecipe){

      final SingleInputRecipe inputRecipe = (SingleInputRecipe) recipe;

      brewingRecipes.put(inputRecipe.getInput(),(BrewRecipe)recipe);
    }

    if(recipe instanceof FurnaceRecipe){

      //TODO setup for multi input

      final SingleInputRecipe inputRecipe = (SingleInputRecipe) recipe;

      furnaceRecipes.put(inputRecipe.getInput(), (FurnaceRecipe) recipe);
    }

  }


}
