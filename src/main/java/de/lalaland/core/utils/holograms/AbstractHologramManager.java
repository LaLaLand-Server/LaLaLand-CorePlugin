package de.lalaland.core.utils.holograms;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import de.lalaland.core.utils.common.UtilChunk;
import de.lalaland.core.utils.events.PlayerReceiveChunkEvent;
import de.lalaland.core.utils.events.PlayerUnloadsChunkEvent;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public abstract class AbstractHologramManager implements Listener {

  public AbstractHologramManager(JavaPlugin host, IHologramFactory factory) {
    this.factory = factory;
    this.hologramViews = Maps.newHashMap();
    this.entityIDMappings = Maps.newHashMap();
    this.entityIDinverseMappings = Maps.newHashMap();
    this.loadedHolograms = HashBasedTable.<String, Long, Map<Location, AbstractHologram>>create();
    this.movingHolograms = Sets.newHashSet();
    Bukkit.getPluginManager().registerEvents(this, host);
    Bukkit.getOnlinePlayers().forEach(
        player -> this.hologramViews.put(player, new HologramView(player))); // Handle reloads
    Bukkit.getScheduler().runTaskTimer(host, () -> {
      if (this.movingHolograms.isEmpty()) {
        return;
      }
      Set<MovingHologram> removers = Sets.newHashSet();
      for (MovingHologram moving : this.movingHolograms) {
        if (moving.isAlive()) {
          moving.onTick();
        } else {
          removers.add(moving);
        }
      }
      for (MovingHologram remov : removers) {
        this.runOutMovingHologram(remov);
      }
    }, 1L, 1L);
  }

  private final IHologramFactory factory;
  private final Table<String, Long, Map<Location, AbstractHologram>> loadedHolograms;
  private final Map<Player, HologramView> hologramViews;
  private final Map<Integer, AbstractHologram> entityIDMappings;
  private final Map<AbstractHologram, Set<Integer>> entityIDinverseMappings;
  private final Set<MovingHologram> movingHolograms;

  public MovingHologram createMovingHologram(Location location, Vector direction, int ticksAllive) {
    MovingHologram moving = new MovingHologram(this.createHologram(location), direction,
        ticksAllive);
    movingHolograms.add(moving);
    return moving;
  }

  protected void runOutMovingHologram(MovingHologram moving) {
    this.removeHologram(moving.getHologram());
    this.movingHolograms.remove(moving);
  }

  protected void onInteract(Player player, int entityID) {
    if (!this.entityIDMappings.containsKey(entityID)) {
      return;
    }
    this.getRelativeLine(player, this.getHologramFromEntityID(entityID)).onClick(player);
  }

  public Set<Player> getViewing(AbstractHologram hologram) {
    Set<Player> viewers = Sets.newHashSet();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (this.isViewing(player, hologram)) {
        viewers.add(player);
      }
    }
    return viewers;
  }

  private IHologramLine<?> getRelativeLine(Player player, AbstractHologram hologram) {
    Vector playerDirection = player.getEyeLocation().getDirection();
    Vector playerLocation = player.getEyeLocation().toVector();

    Map<Vector, IHologramLine<?>> hologramReferences = Maps.newHashMap();

    for (int index = 0; index < hologram.getSize(); index++) {
      IHologramLine<?> line = hologram.getHologramLine(index);
      Vector lineVector = line.getLocation().toVector().subtract(playerLocation)
          .add(new Vector(0, 0.48, 0));
      hologramReferences.put(lineVector, line);
    }

    return hologramReferences.get(hologramReferences.keySet().stream().min(
        (vec1, vec2) -> Float.compare(vec1.angle(playerDirection), vec2.angle(playerDirection)))
        .get());
  }

  private AbstractHologram getHologramFromEntityID(int id) {
    return this.entityIDMappings.get(id);
  }

  protected void setClickableIdentifier(Set<Integer> ids, AbstractHologram hologram) {
    this.entityIDinverseMappings.put(hologram, ids);
    for (Integer id : ids) {
      this.entityIDMappings.put(id, hologram);
    }
  }

  @EventHandler
  public void onChunkReceiving(PlayerReceiveChunkEvent event) {
    String worldName = event.getPlayer().getWorld().getName();
    Long chunkID = event.getChunkKey();
    Player player = event.getPlayer();

    if (!this.loadedHolograms.contains(worldName, chunkID)) {
      return;
    }

    for (AbstractHologram hologram : this.loadedHolograms.get(worldName, chunkID).values()) {
      HologramView view = this.getViewOf(player);
      if (hologram.isViableViewer(player)) {
        view.addHologram(hologram);
      }
    }
  }

  @EventHandler
  public void onChunkUnload(PlayerUnloadsChunkEvent event) {
    String worldName = event.getPlayer().getWorld().getName();
    Long chunkID = event.getChunkKey();
    Player player = event.getPlayer();

    if (!this.loadedHolograms.contains(worldName, chunkID)) {
      return;
    }

    for (AbstractHologram hologram : this.loadedHolograms.get(worldName, chunkID).values()) {
      HologramView view = this.getViewOf(player);
      if (view.isViewing(hologram)) {
        view.removeHologram(hologram);
      }
    }
  }

  public AbstractHologram createHologram(Location location) {
    return this.createHologram(location, (player) -> true);
  }

  public AbstractHologram createHologram(Location location, Predicate<Player> viewFilter) {
    World world = location.getWorld();
    Long chunkID = UtilChunk.getChunkKey(location);
    Map<Location, AbstractHologram> chunkHolograms;
    if (!this.loadedHolograms.contains(world.getName(), chunkID)) {
      this.loadedHolograms.put(world.getName(), chunkID, Maps.newHashMap());
    }

    chunkHolograms = this.loadedHolograms.get(world.getName(), chunkID);

    AbstractHologram hologram = factory.supplyHologram(location, viewFilter, this);

    chunkHolograms.put(location, hologram);

    for (Player player : location.getWorld().getPlayers()) {
      if (UtilChunk.getChunkViews(player).contains(chunkID)) {
        HologramView view = this.getViewOf(player);
        if (hologram.isViableViewer(player)) {
          view.addHologram(hologram);
        }
      }
    }

    return hologram;
  }

  public void removeHologram(AbstractHologram hologram) {
    for (HologramView view : this.hologramViews.values()) {
      if (view.isViewing(hologram)) {
        view.removeHologram(hologram);
      }
    }
    Location holoLoc = hologram.getBaseLocation();
    World holoWorld = holoLoc.getWorld();
    Long chunkID = UtilChunk.getChunkKey(holoLoc);
    Map<Location, AbstractHologram> chunkMap = this.loadedHolograms
        .get(holoWorld.getName(), chunkID);
    chunkMap.remove(holoLoc);
    if (this.entityIDinverseMappings.containsKey(hologram)) {
      for (Integer id : this.entityIDinverseMappings.get(hologram)) {
        this.entityIDMappings.remove(id);
      }
      this.entityIDinverseMappings.remove(hologram);
    }
    if (chunkMap.isEmpty()) {
      this.loadedHolograms.remove(holoWorld.getName(), chunkID);
    }
  }

  public boolean isViewing(Player player, AbstractHologram hologram) {
    return this.hologramViews.get(player).isViewing(hologram);
  }

  public HologramView getViewOf(Player player) {
    return this.hologramViews.get(player);
  }

  @EventHandler
  public void handleJoin(PlayerJoinEvent event) {
    this.hologramViews.put(event.getPlayer(), new HologramView(event.getPlayer()));
  }

  @EventHandler
  public void handleQuit(PlayerQuitEvent event) {
    this.hologramViews.remove(event.getPlayer());
  }
}