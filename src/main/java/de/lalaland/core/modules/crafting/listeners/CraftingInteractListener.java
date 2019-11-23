package de.lalaland.core.modules.crafting.listeners;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.ui.gui.PrivateGui;
import de.lalaland.core.ui.gui.impl.IGui;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@AllArgsConstructor
public class CraftingInteractListener implements Listener {

  private final CorePlugin corePlugin;

  @EventHandler
  public void onInteractOnWorkbench(final PlayerInteractEvent event){
    final Player player = event.getPlayer();
    final Block block = event.getClickedBlock();

    if(block == null || block.getType() != Material.CRAFTING_TABLE){
      return;
    }

    getNewCraftingGui().open(player);
  }

  private IGui getNewCraftingGui(){

    final IGui gui = new PrivateGui(corePlugin.getGuiManager(),"Werkbank",5,true) {};

    return gui;
  }

}
