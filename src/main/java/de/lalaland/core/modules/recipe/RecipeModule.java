package de.lalaland.core.modules.recipe;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.recipe.furnace.FurnaceRecipe;
import de.lalaland.core.utils.items.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RecipeModule implements IModule {

  @Getter
  private RecipeManager recipeManager;

  @Override
  public String getModuleName() {
    return "RecipeModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {
    recipeManager = new RecipeManager();
    registerAllRecipes();
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }

  private void registerAllRecipes(){
    final int burntimeInSeconds = 10;
    final ItemStack furnaceResult = new ItemStack(Material.DIAMOND);
    final FurnaceRecipe test1 = new FurnaceRecipe(burntimeInSeconds) {
      @Override
      public int getRequiredLevel() {
        return 0;
      }

      @Override
      public ItemStack getResultItem() {
        return new ItemBuilder(Material.EMERALD).build();
      }

      @Override
      public double getIngameXp() {
        return 0.2;
      }

      @Override
      public long getExp() {
        return 1;
      }

      @Override
      public ItemStack getInput() {
        return furnaceResult;
      }
    };

    getRecipeManager().registerRecipe(test1);

  }

}
