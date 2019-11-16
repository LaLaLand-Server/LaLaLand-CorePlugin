package de.lalaland.core.utils.items.display;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import de.lalaland.core.utils.nbtapi.NBTItem;
import de.lalaland.core.utils.nbtapi.NBTList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemDisplayCompiler extends PacketAdapter implements Function<ItemStack, ItemStack> {

  public static final String NBT_KEY = "CompileDisplay";

  public static void addDisplayCompileKey(String key, ItemStack item) {
    addDisplayCompileKey(key, new NBTItem(item));
  }

  public static void addDisplayCompileKey(String key, NBTItem nbt) {
    NBTList<String> compilerList = nbt.getStringList(NBT_KEY);
    compilerList.add(key);
  }

  public ItemDisplayCompiler(Plugin plugin) {
    super(plugin, Server.WINDOW_ITEMS, Server.HELD_ITEM_SLOT);
    this.compilerMap = new Object2ObjectOpenHashMap<String, DisplayConverter>();
  }

  @Override
  public void onPacketReceiving(PacketEvent event) {
    PacketContainer packet = event.getPacket();
    this.playerToModify = event.getPlayer();
    StructureModifier<ItemStack> structMod = packet.getItemModifier();
    for (int index = 0; index < structMod.size(); index++) {
      structMod.modify(index, this::apply);
    }
  }

  private final Object2ObjectOpenHashMap<String, DisplayConverter> compilerMap;
  private Player playerToModify;

  public void registerConverter(DisplayConverter converter) {
    this.compilerMap.put(converter.getDisplayKey(), converter);
  }

  @Override
  public ItemStack apply(ItemStack itemStack) {
    NBTItem nbt = new NBTItem(itemStack);
    if (!nbt.hasKey(NBT_KEY)) {
      return itemStack;
    }
    DisplayConverter converter = this.compilerMap.get(nbt.getString(NBT_KEY));
    if (converter == null) {
      return itemStack;
    }
    return converter.compileInfo(playerToModify, itemStack).getResult();
  }
}