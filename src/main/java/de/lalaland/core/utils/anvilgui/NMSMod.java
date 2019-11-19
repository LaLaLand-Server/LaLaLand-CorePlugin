package de.lalaland.core.utils.anvilgui;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class NMSMod {

  protected static final CraftServer craftServer = (CraftServer) Bukkit.getServer();
  protected static final MinecraftServer minecraftServer = craftServer.getServer();

  protected void sendPacket(Packet<?> packet, EntityPlayer receiver) {
    receiver.playerConnection.sendPacket(packet);
  }

  protected void sendPacket(Packet<?> packet, List<EntityPlayer> receivers) {
    receivers.forEach((a) -> sendPacket(packet, a));
  }

  protected EntityPlayer toEntityPlayer(Player player) {
    return ((CraftPlayer) player).getHandle();
  }

  protected List<EntityPlayer> toEntityPlayers(List<Player> x) {
    return x.stream().map(player -> ((CraftPlayer) player).getHandle())
        .collect(Collectors.toList());
  }

  protected List<EntityPlayer> castEntityPlayers(List<Object> x) {
    return x.stream().map(player -> (EntityPlayer) player).collect(Collectors.toList());
  }

  protected List<Entity> castEntities(List<Object> x) {
    return x.stream().map(player -> (Entity) player).collect(Collectors.toList());
  }
}
