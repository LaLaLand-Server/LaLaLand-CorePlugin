package de.lalaland.core.modules.chat.channels;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.chat.listeners.AsyncPlayerChatListener;
import de.lalaland.core.modules.resourcepack.skins.Model;
import de.lalaland.core.ui.gui.PrivateGui;
import de.lalaland.core.ui.gui.icon.ClickableIcon;
import de.lalaland.core.ui.gui.impl.IGui;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class ChatChannelManager {

  private final CorePlugin corePlugin;
  @Getter
  private final Object2ObjectOpenHashMap<ChatChannel, ObjectSet<Player>> playerChatChannels;
@Getter
  private final Object2ObjectOpenHashMap<Player, ChatChannel> singlePlayerChannels;

  public ChatChannelManager(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;
    playerChatChannels = new Object2ObjectOpenHashMap<>();
    singlePlayerChannels = new Object2ObjectOpenHashMap<>();
    for(final ChatChannel channel : ChatChannel.values()){
      playerChatChannels.put(channel, ObjectSets.emptySet());
    }
    Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(this),corePlugin);
  }

  public ChatChannel getPlayerChatChannel(final Player player){

    if(!singlePlayerChannels.containsKey(player)){
      return ChatChannel.GLOBAL;
    }

    return singlePlayerChannels.get(player);
  }

  private void switchChatChannel(final Player player, final ChatChannel chatChannel){

    final ChatChannel current = getPlayerChatChannel(player);

    if(current == chatChannel){
      return;
    }

    playerChatChannels.get(current).remove(player);
    playerChatChannels.get(chatChannel).add(player);
    singlePlayerChannels.put(player,chatChannel);
  }

  public ObjectSet<Player> getReceivers(final ChatChannel chatChannel){
    return playerChatChannels.get(chatChannel);
  }

  public IGui getChannelChooseGui(){

    final PrivateGui gui = new PrivateGui(corePlugin.getGuiManager(),"Channel wählen",
        InventoryType.HOPPER,
        true) {};

    gui.setIcon(1,
        getIconForChannelSwitchGui(ChatChannel.GLOBAL, Model.RED_X),false);
    gui.setIcon(3,
        getIconForChannelSwitchGui(ChatChannel.TRADING, Model.RED_X),true);

    return gui;
  }

  private ClickableIcon getIconForChannelSwitchGui(final ChatChannel chatChannel, final Model model){

    return new ClickableIcon(model.getItem()) {
      @Override
      public void handleRightClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        switchChatChannel(player,chatChannel);
        player.closeInventory();
      }

      @Override
      public void handleLeftClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        switchChatChannel(player,chatChannel);
        player.closeInventory();
      }
    };
  }


}
