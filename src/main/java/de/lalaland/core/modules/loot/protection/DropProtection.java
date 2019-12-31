package de.lalaland.core.modules.loot.protection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.lalaland.core.utils.common.NameSpaceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 31.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DropProtection {

  private static final Gson GSON = new GsonBuilder().create();
  private static final String PROTECT_KEY = "_DROP_VALIDATOR";

  public static void protect(Item drop, UUID... viables) {

    JsonArray ids;
    NamespacedKey nsk = NameSpaceFactory.provide(PROTECT_KEY);
    PersistentDataContainer container = drop.getPersistentDataContainer();

    if (drop.getScoreboardTags().contains(PROTECT_KEY)) {
      ids = GSON.fromJson(container.get(nsk, PersistentDataType.STRING), JsonArray.class);
    } else {
      drop.getScoreboardTags().add(PROTECT_KEY);
      ids = new JsonArray();
    }

    for (UUID viableID : viables) {
      ids.add(viableID.toString());
    }

    container.set(nsk, PersistentDataType.STRING, GSON.toJson(ids));

  }

  @Nullable
  public static List<UUID> getViables(Item drop) {
    List<UUID> ids;
    if (!drop.getScoreboardTags().contains(PROTECT_KEY)) {
      return null;
    }
    NamespacedKey key = NameSpaceFactory.provide(PROTECT_KEY);
    JsonArray array = GSON.fromJson(drop.getPersistentDataContainer().get(key, PersistentDataType.STRING), JsonArray.class);
    ids = new ArrayList<>();

    for (JsonElement element : array) {
      ids.add(UUID.fromString(element.getAsString()));
    }

    return ids;
  }

  public static boolean canPickup(Item drop, UUID pickupID) {
    List<UUID> viables = getViables(drop);
    if (viables == null) {
      return true;
    }
    for (UUID viable : viables) {
      if (viable.equals(pickupID)) {
        return true;
      }
    }
    return false;
  }

}
