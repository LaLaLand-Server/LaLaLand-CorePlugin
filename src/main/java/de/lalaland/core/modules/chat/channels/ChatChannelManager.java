package de.lalaland.core.modules.chat.channels;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.chat.listeners.AsyncPlayerChatListener;
import de.lalaland.core.ui.gui.PrivateGui;
import de.lalaland.core.ui.gui.impl.IGui;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatChannelManager {

  private final CorePlugin corePlugin;
  @Getter
  private final Object2ObjectOpenHashMap<ChatChannel, ObjectSet<Player>> playerChatChannels;

  public ChatChannelManager(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;
    playerChatChannels = new Object2ObjectOpenHashMap<>();
    for(final ChatChannel channel : ChatChannel.values()){
      playerChatChannels.put(channel, ObjectSets.emptySet());
    }
    Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(this),corePlugin);
  }

  public ChatChannel getPlayerChatChannel(final Player player){

    for(final ChatChannel channels : playerChatChannels.keySet()){
      if(playerChatChannels.get(channels).contains(player)){
        return channels;
      }
    }
    return ChatChannel.GLOBAL;
  }

  public void switchChatChannel(final Player player, final ChatChannel chatChannel){

    final ChatChannel current = getPlayerChatChannel(player);

    if(current == chatChannel){
      return;
    }

    playerChatChannels.get(chatChannel).add(player);
  }

  public ObjectSet<Player> getReceivers(final ChatChannel chatChannel){
    return playerChatChannels.get(chatChannel);
  }

  public IGui getChannelChooseGui(final Player player){

    final PrivateGui gui = new PrivateGui(corePlugin.getGuiManager(),"Channel wählen",3,
        true) {

    };

    return gui;
  }

}
