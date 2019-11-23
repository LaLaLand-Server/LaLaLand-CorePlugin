package de.lalaland.core.modules.recipe;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.user.User;
import org.bukkit.entity.Player;

public abstract class Recipe implements IRecipe {

  public void pickUpResult(final CorePlugin corePlugin, final Player player){
    final User user = corePlugin.getUserManager().getUser(player.getUniqueId());
    user.addExp(getExp());
    player.getInventory().addItem(getResultItem());
  }

}
