package de.lalaland.core.utils.common;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.lalaland.core.CorePlugin;
import de.lalaland.core.utils.events.PlayerReceiveEntityEvent;
import de.lalaland.core.utils.events.PlayerUnloadsEntityEvent;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
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
  private static final Map<String, String[]> TEXTURE_CASHE = new Object2ObjectOpenHashMap<>();
  private static final Map<String, UUID> NAME_CASHE = new Object2ObjectOpenHashMap<>();
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

  public static String getEncodedTexture(final String url) {
    final byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    return new String(encodedData);
  }

  public static String[] getSkinFromID(final UUID id) {
    final String uuid = id.toString().replaceAll("-", "");
    if (TEXTURE_CASHE.containsKey(uuid)) {
      return TEXTURE_CASHE.get(uuid);
    }
    try {
      final URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
      final InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
      final JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0)
          .getAsJsonObject();
      final String texture = textureProperty.get("value").getAsString();
      final String signature = textureProperty.get("signature").getAsString();

      final String[] infos = new String[]{texture, signature};

      TEXTURE_CASHE.put(uuid, infos);

      return infos;
    } catch (final IOException e) {
      System.err.println("Could not get skin data from session servers!");
      e.printStackTrace();
      return null;
    }
  }

  public static String[] getSkinFromName(final String name) {
    final UUID uuid;
    if (NAME_CASHE.containsKey(name)) {
      uuid = NAME_CASHE.get(name);
    } else {
      try {
        final URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        final InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
        uuid = UtilText.uuidFromShortString(new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString());
        NAME_CASHE.put(name, uuid);
      } catch (final IOException e) {
        System.err.println("Could not get skin data from session servers!");
        e.printStackTrace();
        return null;
      }
    }
    return getSkinFromID(uuid);
  }

  @Override
  public void run() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      attackCooldowns.put(player, player.getCooledAttackStrength(0F));
    }
  }
}
