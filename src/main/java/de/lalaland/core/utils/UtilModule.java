package de.lalaland.core.utils;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.utils.actionbar.ActionBarManager;
import de.lalaland.core.utils.common.NameSpaceFactory;
import de.lalaland.core.utils.common.UtilChunk;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import de.lalaland.core.utils.holograms.impl.infobar.InfoBar;
import de.lalaland.core.utils.holograms.infobars.InfoBarManager;
import de.lalaland.core.utils.items.display.ItemDisplayCompiler;
import de.lalaland.core.utils.packets.adapter.ChunkTracker;
import de.lalaland.core.utils.packets.adapter.EntityTracker;
import lombok.Getter;
import net.crytec.inventoryapi.anvil.AnvilGUI;
import net.crytec.libs.protocol.ProtocolAPI;
import net.crytec.libs.protocol.npc.NpcAPI;
import net.crytec.libs.protocol.skinclient.PlayerSkinManager;
import net.crytec.libs.protocol.tablist.TabListManager;
import net.crytec.libs.protocol.tablist.implementation.EmptyTablist;

public class UtilModule implements IModule {

  @Getter
  private HologramManager hologramManager;
  @Getter
  private ActionBarManager actionBarManager;
  @Getter
  private InfoBarManager infoBarManager;
  @Getter
  private ProtocolAPI protocolAPI;
  @Getter
  private NpcAPI npcAPI;
  @Getter
  private TabListManager tabListManager;
  @Getter
  private PlayerSkinManager playerSkinManager;

  private EmptyTablist et;

  @Override
  public String getModuleName() {
    return "Utility Module";
  }

  @Override
  public void enable(final CorePlugin plugin) throws Exception {
    NameSpaceFactory.init(plugin);
    ChunkTracker.init(plugin, plugin.getProtocolManager());
    EntityTracker.init(plugin, plugin.getProtocolManager());
    UtilChunk.init(plugin);
    UtilPlayer.init(plugin);
    plugin.setDisplayCompiler(new ItemDisplayCompiler(plugin));
    plugin.getProtocolManager().addPacketListener(plugin.getDisplayCompiler());
    hologramManager = new HologramManager(plugin);
    playerSkinManager = new PlayerSkinManager();
    plugin.getCommandManager().registerCommand(new TestCommand(hologramManager));
    actionBarManager = new ActionBarManager(plugin);
    infoBarManager = new InfoBarManager(plugin, (entity) -> new InfoBar(entity, infoBarManager));
    protocolAPI = new ProtocolAPI(plugin);
    npcAPI = new NpcAPI(plugin);
    tabListManager = new TabListManager(plugin, (p) -> et);
    et = new EmptyTablist(tabListManager);
//
//    final Skin skin = Model.AC_SEMIDIRT.getSkin();
//    for (int i = 0; i < 80; i++) {
//      final TabLine tl = new TabLine(i, " - ");
//      tl.setTexture(skin.data.texture.signature, skin.data.texture.value);
//      et.addLine(tl);
//    }
//    et.updateAndSendHeaderFooter("\n\n\n" + Model.SERVER_ICON.getChar(),
//        "Test1: " + Model.HP_85.getChar() + "\n" +
//            "Test2: " + Model.HP_85.getChar() + "\n" +
//            "Test3: " + Model.HP_85.getChar() + "\n" +
//            "Test4: " + Model.HP_85.getChar() + "\n" +
//            "Test5: " + Model.HP_85.getChar() + "\n");
//    Bukkit.getScheduler().runTaskLater(plugin, () -> et.updateAndSendHeaderFooter("\n\n\n" + Model.SERVER_ICON.getChar(),
//        "Test11: " + Model.HP_85.getChar() + "   Test12: " + Model.HP_85.getChar() + "\n" +
//            "Test21: " + Model.HP_85.getChar() + "   Test22: " + Model.HP_85.getChar() + "\n" +
//            "Test31: " + Model.HP_85.getChar() + "   Test32: " + Model.HP_85.getChar() + "\n" +
//            "Test41: " + Model.HP_85.getChar() + "   Test42: " + Model.HP_85.getChar() + "\n" +
//            "Test51: " + Model.HP_85.getChar() + "   Test52: " + Model.HP_85.getChar() + "\n"), 100L);
  }

  @Override
  public void disable(final CorePlugin plugin) throws Exception {

  }
}
