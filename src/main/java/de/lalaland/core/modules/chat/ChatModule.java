package de.lalaland.core.modules.chat;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.chat.channels.ChannelChooseCommand;
import de.lalaland.core.modules.chat.channels.ChatChannelManager;
import lombok.Getter;

public class ChatModule implements IModule {

  private final CorePlugin corePlugin;
  @Getter
  private final ChatChannelManager chatChannelManager;

  public ChatModule(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;
    chatChannelManager = new ChatChannelManager(corePlugin);
    corePlugin.getCommandManager().registerCommand(new ChannelChooseCommand(chatChannelManager));
  }

  @Override
  public String getModuleName() {
    return "ChatModule";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {

  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
