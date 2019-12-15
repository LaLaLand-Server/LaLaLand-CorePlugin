package de.lalaland.core.utils.anvilgui;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
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

  protected void sendPacket(final Packet<?> packet, final EntityPlayer receiver) {
    receiver.playerConnection.sendPacket(packet);
  }

  protected void sendPacket(final Packet<?> packet, final List<EntityPlayer> receivers) {
    receivers.forEach((a) -> sendPacket(packet, a));
  }

  protected EntityPlayer toEntityPlayer(final Player player) {
    return ((CraftPlayer) player).getHandle();
  }

  protected List<EntityPlayer> toEntityPlayers(final List<Player> x) {
    return x.stream().map(player -> ((CraftPlayer) player).getHandle())
        .collect(Collectors.toList());
  }

  protected List<EntityPlayer> castEntityPlayers(final List<Object> x) {
    return x.stream().map(player -> (EntityPlayer) player).collect(Collectors.toList());
  }

  protected List<Entity> castEntities(final List<Object> x) {
    return x.stream().map(player -> (Entity) player).collect(Collectors.toList());
  }
}
