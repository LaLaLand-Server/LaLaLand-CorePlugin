package de.lalaland.core.modules.chat.messages;

import co.aikar.commands.BaseCommand;
import de.lalaland.core.CorePlugin;

public class OfflineMessageCommand extends BaseCommand {

  private final CorePlugin corePlugin;

  public OfflineMessageCommand(final CorePlugin corePlugin){
    this.corePlugin = corePlugin;
  }

}
