package de.lalaland.core.modules.recipe.furnace;

import de.lalaland.core.modules.recipe.Recipe;
import de.lalaland.core.modules.recipe.impl.SingleInputRecipe;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class FurnaceRecipe extends Recipe implements SingleInputRecipe {

  @Getter
  private final int burnTime;




}
