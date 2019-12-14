package de.lalaland.core.modules.economy;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.modules.economy.gui.MobileEconomyGui;
import org.bukkit.entity.Player;

public class EconomyModule implements IModule {

  public EconomyModule(final CorePlugin corePlugin) {
    this.corePlugin = corePlugin;
  }

  private final CorePlugin corePlugin;

  @Override
  public void enable(final CorePlugin plugin) throws Exception {

  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }

  @Override
  public String getModuleName() {
    return "EconomyModule";
  }

  public void openMobileEconomyGui(final Player player) {

    final MobileEconomyGui mobileEconomyGui = new MobileEconomyGui(corePlugin, player);
    mobileEconomyGui.getMobileGui().open(player);
  }

}
