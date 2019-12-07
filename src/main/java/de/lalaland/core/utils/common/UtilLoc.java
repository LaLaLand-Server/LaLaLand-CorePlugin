package de.lalaland.core.utils.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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

  public static String locToString(final Location location) throws UnsupportedEncodingException {
    return new String(toBytes(location));
  }

  public static Location locFromString(final String locationString) throws UnsupportedEncodingException {
    return fromBytes(locationString.getBytes());
  }

  public static byte[] toBytes(@NotNull final Location location) {
    final ByteBuffer buffer = ByteBuffer.allocate(48);
    buffer.rewind();
    final UUID worldID = location.getWorld().getUID();
    final double x = location.getX();
    final double y = location.getY();
    final double z = location.getZ();
    final float pitch = location.getPitch();
    final float yaw = location.getYaw();

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
  public static Location fromBytes(final byte[] bytes) {
    final ByteBuffer buffer = ByteBuffer.wrap(bytes);
    buffer.rewind();
    final long idMSB = buffer.getLong();
    final long idLSB = buffer.getLong();
    final double x = buffer.getDouble();
    final double y = buffer.getDouble();
    final double z = buffer.getDouble();
    final float pitch = buffer.getFloat();
    final float yaw = buffer.getFloat();

    final World world = Bukkit.getWorld(new UUID(idMSB, idLSB));
    if (world == null) {
      return null;
    }
    return new Location(world, x, y, z, yaw, pitch);
  }

}
