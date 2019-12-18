package de.lalaland.core.modules.economy.gui;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.utils.anvilgui.AnvilGUI;
import lombok.Getter;
import org.bukkit.entity.Player;

public class MobileEconomyGui {

  private Player player;
  @Getter
  private final CorePlugin corePlugin;

  public MobileEconomyGui(final CorePlugin corePlugin, final Player player) {
    this.corePlugin = corePlugin;
  }

  private void openInputForDrop(final Player player) {

    final AnvilGUI anvilGUI = new AnvilGUI(player, "Betrag", (player1, s) -> {

      return null;
    });
  }


}
