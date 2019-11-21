package de.lalaland.core.modules.chat;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;

public class ChatModule implements IModule {

  private final CorePlugin corePlugin;

  public ChatModule(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;

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
