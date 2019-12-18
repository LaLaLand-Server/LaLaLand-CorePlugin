package de.lalaland.core.modules.chat.channels;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandPermission("user")
@CommandAlias("channel")
public class ChannelChooseCommand extends BaseCommand {

  private final ChatChannelManager chatChannelManager;

  public ChannelChooseCommand(final ChatChannelManager chatChannelManager){
    this.chatChannelManager = chatChannelManager;
  }

  @Subcommand("gui")
  public void openGui(final Player player){

  }



}
