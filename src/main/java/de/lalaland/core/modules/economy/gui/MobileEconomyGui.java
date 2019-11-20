package de.lalaland.core.modules.economy.gui;

import de.lalaland.core.ui.gui.PrivateGui;
import lombok.Getter;
import org.bukkit.entity.Player;

public class MobileEconomyGui {

  private Player player;
  @Getter
  private final PrivateGui mobileGui;

  public MobileEconomyGui(final Player player) {
    mobileGui = createMobileGui(player);
  }

  private PrivateGui createMobileGui(final Player player) {

  }


}
