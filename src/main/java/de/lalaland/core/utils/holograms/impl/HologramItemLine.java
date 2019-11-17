package de.lalaland.core.utils.holograms.impl;

import de.lalaland.core.utils.holograms.AbstractHologramItemLine;
import de.lalaland.core.utils.packets.wrapper.WrapperPlayServerMount;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EntityItem;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import net.minecraft.server.v1_14_R1.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HologramItemLine extends AbstractHologramItemLine {

  public HologramItemLine(Location location, ItemStack itemStack, Hologram hologram) {
    super(location, itemStack, hologram);
    this.itemEntity = new ItemEntity(location, itemStack);
    this.itemVehicle = new ItemVehicle(location.clone().add(0, -0.1, 0));
    this.packets = new Packet<?>[6];
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.packets[3] = this.itemVehicle.getSpawnPacket();
    this.packets[4] = this.itemVehicle.getDespawnPacket();
    this.packets[5] = this.itemVehicle.getHidePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);
  }

  private ItemEntity itemEntity;
  private final ItemVehicle itemVehicle;
  private final Packet<?>[] packets;
  private WrapperPlayServerMount mountPacket;

  @Override
  public void showTo(Player player) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    connection.sendPacket(this.packets[0]);
    connection.sendPacket(this.packets[3]);
    connection.sendPacket(this.packets[2]);
    connection.sendPacket(this.packets[5]);
    mountPacket.sendPacket(player);
  }

  @Override
  public void hideFrom(Player player) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    connection.sendPacket(this.packets[1]);
    connection.sendPacket(this.packets[4]);
  }

  @Override
  public void update(ItemStack newValue) {
    this.itemEntity.setItemStack(CraftItemStack.asNMSCopy(newValue));
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);

    for (Player player : this.getHostingHologram().getViewers()) {
      PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
      connection.sendPacket(this.packets[2]);
      connection.sendPacket(this.packets[5]);
    }
  }

  private final class ItemVehicle extends EntityArmorStand {

    public ItemVehicle(Location location) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY() + 0.2,
          location.getZ());
      this.setMarker(true);
      this.setInvisible(true);
      this.setCustomNameVisible(false);
    }

    public PacketPlayOutSpawnEntity getSpawnPacket() {
      return new PacketPlayOutSpawnEntity(this);
    }

    public PacketPlayOutEntityMetadata getHidePacket() {
      return new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
    }

    public PacketPlayOutEntityDestroy getDespawnPacket() {
      return new PacketPlayOutEntityDestroy(this.getId());
    }

    public WrapperPlayServerMount getMountPacket(EntityItem item) {
      WrapperPlayServerMount packet = new WrapperPlayServerMount();

      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{item.getId()});

      return packet;
    }

  }

  private final class ItemEntity extends EntityItem {

    private ItemEntity(Location location, ItemStack item) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(),
          location.getZ(), CraftItemStack.asNMSCopy(item));
      this.setInvulnerable(true);
      this.setNoGravity(true);
      this.setMot(new Vec3D(0, 0, 0));
      this.velocityChanged = true;
    }

    @Override
    public void tick() {

    }

    public PacketPlayOutSpawnEntity getSpawnPacket() {
      return new PacketPlayOutSpawnEntity(this);
    }

    public PacketPlayOutEntityDestroy getDespawnPacket() {
      return new PacketPlayOutEntityDestroy(this.getId());
    }

    public PacketPlayOutEntityMetadata getUpdatePacket() {
      return new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
    }

  }

  @Override
  public void sendMove(Player player, Vector direction) {
    PacketPlayOutRelEntityMove movePacket = new PacketPlayOutRelEntityMove(this.itemEntity.getId(),
        (short) (direction.getX() * 4096), (short) (direction.getY() * 4096),
        (short) (direction.getZ() * 4096), false);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(movePacket);
  }

}
