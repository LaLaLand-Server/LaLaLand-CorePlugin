package de.lalaland.core.utils.common;

import com.google.common.base.Preconditions;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.utils.events.PlayerReceiveEntityEvent;
import de.lalaland.core.utils.events.PlayerUnloadsEntityEvent;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
public class UtilPlayer implements Listener, Runnable {

  private static UtilPlayer instance;
  private static final boolean initialized = false;
  private static final Vector UP_VEC = new Vector(0, 1, 0);
  private static final Vector DOWN_VEC = new Vector(0, -1, 0);
  private final Object2ObjectOpenHashMap<Player, IntSet> playerViews;
  private final Object2FloatOpenHashMap<Player> attackCooldowns;

  private UtilPlayer() {
    playerViews = new Object2ObjectOpenHashMap<>();
    Bukkit.getOnlinePlayers()
        .forEach(player -> playerViews.put(player, new IntOpenHashSet())); //  fix for reloads
    attackCooldowns = new Object2FloatOpenHashMap<>();
  }

  public static void init(final CorePlugin host) {
    Preconditions.checkArgument(!initialized, "UtilPlayer is already initialized!");
    instance = new UtilPlayer();
    Bukkit.getPluginManager().registerEvents(instance, host);
    host.getTaskManager().runRepeatedBukkit(instance, 1L, 1L);
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    playerViews.put(event.getPlayer(), new IntOpenHashSet());
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    attackCooldowns.remove(event.getPlayer());
    playerViews.remove(event.getPlayer());
  }

  @EventHandler
  public void onEntityShowing(final PlayerReceiveEntityEvent event) {
    playerViews.get(event.getPlayer()).add(event.getEntityID());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityHiding(final PlayerUnloadsEntityEvent event) {
    final IntSet ints = playerViews.get(event.getPlayer());
    for (final int id : event.getEntityIDs()) {
      ints.remove(id);
    }
  }

  public static float getAttackCooldown(final Player player) {
    return instance.attackCooldowns.getFloat(player);
  }

  public static IntSet getEntityViewOf(final Player player) {
    return instance.playerViews.get(player);
  }

  public static BlockFace getExactFacing(final Player player) {
    final Vector direction = player.getEyeLocation().getDirection();
    if (direction.angle(UP_VEC) <= (Math.PI / 4)) {
      return BlockFace.UP;
    } else if (direction.angle(DOWN_VEC) <= (Math.PI / 4)) {
      return BlockFace.DOWN;
    }
    return player.getFacing();
  }

  public static void playSound(final Player player, final Sound sound, final float volume, final float pitch) {
    player.playSound(player.getEyeLocation(), sound, pitch, volume);
  }

  public static void playSound(final Player player, final Sound sound) {
    playSound(player, sound, 1F, 1F);
  }

  @Override
  public void run() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      attackCooldowns.put(player, player.getCooledAttackStrength(0F));
    }
  }
}
