package de.lalaland.core.utils.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilLoc {

  public static String locToString(Location location) {
    return new String(toBytes(location));
  }

  public static Location locFromString(String locationString) {
    return fromBytes(locationString.getBytes());
  }

  public static byte[] toBytes(@NotNull Location location) {
    ByteBuffer buffer = ByteBuffer.allocate(48);
    buffer.rewind();
    UUID worldID = location.getWorld().getUID();
    double x = location.getX();
    double y = location.getY();
    double z = location.getZ();
    float pitch = location.getPitch();
    float yaw = location.getYaw();

    buffer.putLong(worldID.getMostSignificantBits());
    buffer.putLong(worldID.getLeastSignificantBits());
    buffer.putDouble(x);
    buffer.putDouble(y);
    buffer.putDouble(z);
    buffer.putFloat(pitch);
    buffer.putFloat(yaw);

    return buffer.array();
  }

  @Nullable
  public static Location fromBytes(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    buffer.rewind();
    long idMSB = buffer.getLong();
    long idLSB = buffer.getLong();
    double x = buffer.getDouble();
    double y = buffer.getDouble();
    double z = buffer.getDouble();
    float pitch = buffer.getFloat();
    float yaw = buffer.getFloat();

    World world = Bukkit.getWorld(new UUID(idMSB, idLSB));
    if (world == null) {
      return null;
    }
    return new Location(world, x, y, z, yaw, pitch);
  }

}
