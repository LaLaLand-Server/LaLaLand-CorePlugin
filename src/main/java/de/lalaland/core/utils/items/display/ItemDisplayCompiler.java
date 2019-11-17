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
    super(plugin, Server.WINDOW_ITEMS, Server.SET_SLOT);
    this.compilerMap = new Object2ObjectOpenHashMap<String, DisplayConverter>();
  }

  @Override
  public void onPacketSending(PacketEvent event) {
    PacketContainer packet = event.getPacket();
    this.playerToModify = event.getPlayer();
    if (event.getPacketType() == Server.WINDOW_ITEMS) {
      StructureModifier<ItemStack[]> structMod = packet.getItemArrayModifier();
      for (int index = 0; index < structMod.size(); index++) {
        ItemStack[] itemArray = structMod.read(index);
        for (int i = 0; i < itemArray.length; i++) {
          itemArray[i] = this.apply(itemArray[i]);
        }
      }
    } else {
      StructureModifier<ItemStack> structMod = packet.getItemModifier();
      for (int index = 0; index < structMod.size(); index++) {
        structMod.modify(index, this::apply);
      }
    }
    event.setPacket(packet);
  }

  private final Object2ObjectOpenHashMap<String, DisplayConverter> compilerMap;
  private Player playerToModify;

  public void registerConverter(DisplayConverter converter) {
    this.compilerMap.put(converter.getDisplayKey(), converter);
  }

  @Override
  public ItemStack apply(final ItemStack itemStack) {
    if(itemStack == null) return itemStack;
    ItemStack clone = itemStack.clone();
    NBTItem nbt = new NBTItem(clone);
    if (!nbt.hasKey(NBT_KEY)) {
      return itemStack;
    }
    NBTList<String> displayKeys = nbt.getStringList(NBT_KEY);
    for(int index = 0; index < displayKeys.size(); index++){
      String key = displayKeys.get(index);
      DisplayConverter converter = this.compilerMap.get(key);
      if (converter != null) {
        clone = converter.compileInfo(this.playerToModify, clone).getResult();
      }
    }
    return clone;
  }
}