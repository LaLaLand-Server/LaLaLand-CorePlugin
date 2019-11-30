package de.lalaland.core.modules.schematics.editor;

import com.google.common.collect.Maps;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.utils.tuples.Pair;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EditorSessions implements Listener {

  private final HashMap<UUID, Pair<Location, Location>> session;
  private final HashMap<UUID, Boolean> sessionDone;

  // TODO remove in live version
  public EditorSessions(final CorePlugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
    session = Maps.newHashMap();
    sessionDone = Maps.newHashMap();
  }

  public boolean hasSession(final Player player) {
    return session.containsKey(player.getUniqueId());
  }

  public void addSession(final Player player) {
    session.put(player.getUniqueId(), Pair.of(null, null));
    sessionDone.put(player.getUniqueId(), false);
  }

  public void removeSession(final Player player) {
    session.remove(player.getUniqueId());
    sessionDone.remove(player.getUniqueId());
  }

  public Pair<Location, Location> getSelectedSession(final Player player) {
    final Pair<Location, Location> pair = session.get(player.getUniqueId());
    return pair;
  }

  @EventHandler
  public void onSessionInteract(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();
    if (!session.containsKey(player.getUniqueId())) {
      return;
    }
    if (sessionDone.get(player.getUniqueId())) {
      return;
    }
    event.setCancelled(true);

    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    final Action action = event.getAction();

    if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) {
      return;
    }

    if (action == Action.RIGHT_CLICK_BLOCK) {
      if (session.get(player.getUniqueId()).getLeft() == null) {
        player.sendMessage("Bitte setze erst Position 1");
        return;
      }
      if (session.get(player.getUniqueId()).getLeft().getWorld() != player.getWorld()) {
        player.sendMessage("Position 1 befindet sich in einer anderen Welt!");
        return;
      }
      session.get(player.getUniqueId()).setRight(event.getClickedBlock().getLocation());
      player.sendMessage("Position 2 wurde festgelegt");
    }

    if (action == Action.LEFT_CLICK_BLOCK) {
      session.put(player.getUniqueId(), Pair.of(event.getClickedBlock().getLocation(), null));
      player.sendMessage("Position 1 wurde festgelegt");
    }

    final Pair<Location, Location> locs = session.get(player.getUniqueId());
    if (locs.getRight() != null && locs.getLeft() != null) {
      sessionDone.put(player.getUniqueId(), true);
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    removeSession(event.getPlayer());
  }
}
