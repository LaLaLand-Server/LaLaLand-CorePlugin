package de.lalaland.core.utils;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.IModule;
import de.lalaland.core.utils.common.UtilChunk;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.holograms.impl.HologramManager;
import de.lalaland.core.utils.items.display.ItemDisplayCompiler;
import de.lalaland.core.utils.packets.adapter.ChunkTracker;
import de.lalaland.core.utils.packets.adapter.EntityTracker;

public class UtilModule implements IModule {

  private HologramManager hologramManager;

  @Override
  public String getModuleName() {
    return "Utility Module";
  }

  @Override
  public void enable(CorePlugin plugin) throws Exception {
    ChunkTracker.init(plugin, plugin.getProtocolManager());
    EntityTracker.init(plugin, plugin.getProtocolManager());
    UtilChunk.init(plugin);
    UtilPlayer.init(plugin);
    plugin.setDisplayCompiler(new ItemDisplayCompiler(plugin));
    plugin.getProtocolManager().addPacketListener(plugin.getDisplayCompiler());
    this.hologramManager = new HologramManager(plugin);
  }

  @Override
  public void disable(CorePlugin plugin) throws Exception {

  }
}
