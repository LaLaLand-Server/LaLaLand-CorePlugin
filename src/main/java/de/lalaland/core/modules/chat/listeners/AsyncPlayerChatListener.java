package de.lalaland.core.modules.chat.listeners;

import de.lalaland.core.modules.chat.channels.ChatChannel;
import de.lalaland.core.modules.chat.channels.ChatChannelManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class AsyncPlayerChatListener implements Listener {

  private final ChatChannelManager chatChannelManager;

  @EventHandler
  public void onChat(final AsyncPlayerChatEvent event){

    final Player player = event.getPlayer();

    final ChatChannel channel = chatChannelManager.getPlayerChatChannel(player);

    event.getRecipients().clear();
    event.getRecipients().addAll(chatChannelManager.getReceivers(channel));

  }


}
