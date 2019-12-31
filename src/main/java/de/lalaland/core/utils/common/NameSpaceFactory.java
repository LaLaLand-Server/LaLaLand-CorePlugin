package de.lalaland.core.utils.common;

import de.lalaland.core.CorePlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 31.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class NameSpaceFactory {

  public static void init(CorePlugin plugin) {
    if (instance == null) {
      instance = new NameSpaceFactory(plugin);
    }
  }

  private static NameSpaceFactory instance;

  private NameSpaceFactory(CorePlugin plugin) {
    cachedKeys = new Object2ObjectOpenHashMap<>();
    this.plugin = plugin;
  }

  private final CorePlugin plugin;
  private final Map<String, NamespacedKey> cachedKeys;

  public static NamespacedKey provide(String key) {
    NamespacedKey nsk = instance.cachedKeys.get(key);
    if (nsk == null) {
      Plugin plugin;
      nsk = new NamespacedKey(instance.plugin, key);
      instance.cachedKeys.put(key, nsk);
    }
    return nsk;
  }

}
