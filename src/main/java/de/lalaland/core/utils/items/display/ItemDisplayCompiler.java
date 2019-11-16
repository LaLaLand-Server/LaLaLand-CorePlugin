package de.lalaland.core.utils.items.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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
public class ItemDisplayCompiler extends PacketAdapter {

  public static final String NBT_KEY = "CompileDisplay";

  public ItemDisplayCompiler(Plugin plugin, PacketType... types) {
    super(plugin, Server.WINDOW_ITEMS, Server.HELD_ITEM_SLOT);
    this.compilerMap = new Object2ObjectOpenHashMap<String, DisplayConverter>();
  }

  @Override
  public void onPacketReceiving(PacketEvent event) {
    if(event.getPacketType() == Server.WINDOW_ITEMS){

    }else{

    }
  }

  private final Object2ObjectOpenHashMap<String, DisplayConverter> compilerMap;

  public void registerCompiler(DisplayConverter converter) {
    this.compilerMap.put(converter.getDisplayKey(), converter);
  }

}