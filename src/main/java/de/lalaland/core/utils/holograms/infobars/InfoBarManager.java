package de.lalaland.core.utils.holograms.infobars;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.lalaland.core.utils.common.UtilPlayer;
import de.lalaland.core.utils.events.InfoBarCreateEvent;
import de.lalaland.core.utils.events.PlayerReceiveEntityEvent;
import de.lalaland.core.utils.events.PlayerUnloadsEntityEvent;
import de.lalaland.core.utils.tuples.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class InfoBarManager implements Listener {

  public InfoBarManager(JavaPlugin host, Function<Entity, AbstractInfoBar> barSupplier) {
    this.barSupplier = barSupplier;
    this.host = host;
    this.playerViews = Maps.newHashMap();
    this.infoBarMap = new HashMap<Integer, AbstractInfoBar>();
    Bukkit.getPluginManager().registerEvents(this, host);
    this.hologramEntityMappings = new Int2ObjectOpenHashMap<Entity>();
    this.registerPacketListener(host);
    this.runnable = new InfoBarRunnable(host, this);
  }

  private final InfoBarRunnable runnable;
  private final JavaPlugin host;
  private final Int2ObjectOpenHashMap<Entity> hologramEntityMappings;
  private final HashMap<Integer, AbstractInfoBar> infoBarMap;
  private final Function<Entity, AbstractInfoBar> barSupplier;
  protected final Map<Player, Set<AbstractInfoBar>> playerViews;

  public void addMapping(int hologramEntityID, Entity host) {
    hologramEntityMappings.put(hologramEntityID, host);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    this.playerViews.put(event.getPlayer(), Sets.newHashSet());
    runnable.addPlayer(event.getPlayer());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.playerViews.remove(event.getPlayer());
    runnable.removePlayer(event.getPlayer());
  }

  @EventHandler
  protected void onDismount(EntityDismountEvent event) {
    int entityID = event.getDismounted().getEntityId();
    AbstractInfoBar infoBar = infoBarMap.get(entityID);
		if (infoBar == null) {
			return;
		}
    for (Player player : event.getEntity().getWorld().getPlayers()) {
      if (UtilPlayer.getEntityViewOf(player).contains(entityID)) {
        this.playerViews.get(player).add(infoBar);
      }
    }
  }

  @EventHandler
  protected void onMount(EntityMountEvent event) {
    int entityID = event.getMount().getEntityId();
    AbstractInfoBar infoBar = infoBarMap.get(entityID);
		if (infoBar == null) {
			return;
		}
    for (Player player : Sets.newHashSet(infoBar.viewingPlayer)) {
      infoBar.hideFrom(player);
      this.playerViews.get(player).remove(infoBar);
    }
  }

  @EventHandler
  protected void onEntityShowing(PlayerReceiveEntityEvent event) {
    Bukkit.getScheduler().runTaskLater(this.host, () -> {
      AbstractInfoBar bar = infoBarMap.get(event.getEntityID());
      if (bar != null) {
				if (!bar.getEntity().getPassengers().isEmpty()) {
					return;
				}
        this.playerViews.get(event.getPlayer()).add(bar);
      }
    }, 1L);
  }

  @EventHandler
  protected void onEntityHiding(PlayerUnloadsEntityEvent event) {
    for (Integer entityID : event.getEntityIDs()) {
      AbstractInfoBar bar = infoBarMap.get(entityID);
      if (bar != null) {
        bar.hideFrom(event.getPlayer());
        this.playerViews.get(event.getPlayer()).remove(bar);
      }
    }
  }

  @EventHandler
  protected void onDeath(EntityDeathEvent event) {
    Integer entityID = event.getEntity().getEntityId();
    AbstractInfoBar bar = infoBarMap.get(entityID);
    if (bar != null) {
      Set<Player> viewing = Sets.newHashSet(bar.viewingPlayer);
      for (Player viewer : viewing) {
        bar.hideFrom(viewer);
        this.playerViews.get(viewer).remove(bar);
      }
      this.infoBarMap.remove(entityID);
    }
  }

  @EventHandler
  protected void onChunkUnload(ChunkUnloadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      Integer entityID = entity.getEntityId();
      AbstractInfoBar bar = infoBarMap.get(entityID);
      if (bar != null) {
        for (Player player : bar.viewingPlayer) {
          if (player.isOnline()) {
            bar.hideFrom(player);
            this.playerViews.get(player).remove(bar);
          }
        }
        this.infoBarMap.remove(entityID);
      }
    }
  }

  @Nullable
  public AbstractInfoBar getInfoBar(Entity entity) {
    return this.infoBarMap.get(entity.getEntityId());
  }

  @Nullable
  public AbstractInfoBar createInfoBar(Entity entity) {
    Integer entityID = entity.getEntityId();
    AbstractInfoBar bar = this.infoBarMap.get(entityID);
		if (bar != null) {
			return bar;
		}

    InfoBarCreateEvent event = new InfoBarCreateEvent(entity);
    event.callEvent();
		if (event.isCancelled()) {
			return bar;
		}

    bar = this.barSupplier.apply(entity);
    this.infoBarMap.put(entityID, bar);
    List<Pair<String, InfoLineSpacing>> lines = event.getLines();

    for (int i = 0; i < lines.size(); i++) {
      Pair<String, InfoLineSpacing> entry = lines.get(i);
      bar.addLine(entry.getLeft(), entry.getRight());
    }

    for (Player player : entity.getWorld().getPlayers()) {
      if (UtilPlayer.getEntityViewOf(player).contains(entity.getEntityId())) {
        this.playerViews.get(player).add(bar);
      }
    }

    return bar;
  }

  public void removeInfoBar(Entity entity) {
    Integer entityID = entity.getEntityId();
    AbstractInfoBar bar = this.infoBarMap.get(entityID);
		if (bar == null) {
			return;
		}
    for (Player player : bar.viewingPlayer) {
      bar.hideFrom(player);
      this.playerViews.get(player).remove(bar);
    }
    this.infoBarMap.remove(entityID);
  }

  private void registerPacketListener(JavaPlugin plugin) {
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {

      @Override
      public void onPacketReceiving(PacketEvent event) {
        int entityID = event.getPacket().getIntegers().getValues().get(0);
        Entity entity = hologramEntityMappings.get(entityID);
        if (entity != null) {
          event.getPacket().getIntegers().modify(0, (old) -> entity.getEntityId());
        }
      }

    });
  }

}