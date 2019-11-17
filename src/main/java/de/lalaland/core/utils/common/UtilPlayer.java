package de.lalaland.core.utils.common;

import com.google.common.base.Preconditions;
import de.lalaland.core.utils.events.PlayerReceiveEntityEvent;
import de.lalaland.core.utils.events.PlayerUnloadsEntityEvent;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 17.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilPlayer implements Listener {

  private static UtilPlayer instance;
  private static boolean initialized = false;
  private static final Vector UP_VEC = new Vector(0, 1, 0);
  private static final Vector DOWN_VEC = new Vector(0, -1, 0);
  private final Object2ObjectOpenHashMap<Player, IntSet> playerViews;

  private UtilPlayer() {
    this.playerViews = new Object2ObjectOpenHashMap<Player, IntSet>();
    Bukkit.getOnlinePlayers()
        .forEach(player -> playerViews.put(player, new IntOpenHashSet())); //  fix for reloads
  }

  public static void init(JavaPlugin host) {
    Preconditions.checkArgument(!initialized, "UtilPlayer is already initialized!");
    instance = new UtilPlayer();
    Bukkit.getPluginManager().registerEvents(instance, host);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    this.playerViews.put(event.getPlayer(), new IntOpenHashSet());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.playerViews.remove(event.getPlayer());
  }

  @EventHandler
  public void onEntityShowing(PlayerReceiveEntityEvent event) {
    this.playerViews.get(event.getPlayer()).add(event.getEntityID());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityHiding(PlayerUnloadsEntityEvent event) {
    IntSet ints = this.playerViews.get(event.getPlayer());
    for (int id : event.getEntityIDs()) {
      ints.remove(id);
    }
  }

  public static IntSet getEntityViewOf(Player player) {
    return instance.playerViews.get(player);
  }

  public static BlockFace getExactFacing(Player player) {
    Vector direction = player.getEyeLocation().getDirection();
    if (direction.angle(UP_VEC) <= (Math.PI / 4)) {
      return BlockFace.UP;
    } else if (direction.angle(DOWN_VEC) <= (Math.PI / 4)) {
      return BlockFace.DOWN;
    }
    return player.getFacing();
  }

}
