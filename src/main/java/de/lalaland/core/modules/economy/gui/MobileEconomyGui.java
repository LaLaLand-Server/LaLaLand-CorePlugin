package de.lalaland.core.modules.economy.gui;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.resourcepack.skins.ModelItem;
import de.lalaland.core.ui.gui.PrivateGui;
import de.lalaland.core.ui.gui.icon.ClickableIcon;
import de.lalaland.core.ui.gui.icon.SimpleIcon;
import de.lalaland.core.utils.anvilgui.AnvilGUI;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MobileEconomyGui {

  private Player player;
  @Getter
  private final PrivateGui mobileGui;
  private final CorePlugin corePlugin;

  public MobileEconomyGui(final CorePlugin corePlugin, final Player player) {
    this.corePlugin = corePlugin;
    mobileGui = createMobileGui(player);
  }

  private PrivateGui createMobileGui(final Player player) {
    final PrivateGui gui = new PrivateGui(corePlugin.getGuiManager(), "Geldbörse", 3,
        corePlugin.getCoreConfig().isFillGuis()) {
      @Override
      public void open(final Player player) {
        player.openInventory(getBukkitInventory());
      }
    };

    final SimpleIcon moneyOnHand = new SimpleIcon(ModelItem.RED_X.create());

    final ClickableIcon dropMoney = new ClickableIcon(ModelItem.RED_X.create()) {
      @Override
      public void handleRightClick(final InventoryClickEvent event) {

      }

      @Override
      public void handleLeftClick(final InventoryClickEvent event) {

      }
    };

    return gui;
  }

  private void openInputForDrop(final Player player) {

    final AnvilGUI anvilGUI = new AnvilGUI(player, "Betrag", (player1, s) -> {

      return null;
    });
  }


}
