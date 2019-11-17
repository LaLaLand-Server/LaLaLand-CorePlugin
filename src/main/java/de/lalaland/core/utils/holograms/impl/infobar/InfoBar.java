package de.lalaland.core.utils.holograms.impl.infobar;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.lalaland.core.utils.holograms.impl.infobar.DoubleLinkedPacketHost.LinkedPacketType;
import de.lalaland.core.utils.holograms.infobars.AbstractInfoBar;
import de.lalaland.core.utils.holograms.infobars.InfoBarManager;
import de.lalaland.core.utils.holograms.infobars.InfoLineSpacing;
import de.lalaland.core.utils.packets.wrapper.WrapperPlayServerMount;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EntityPig;
import net.minecraft.server.v1_14_R1.EntityRabbit;
import net.minecraft.server.v1_14_R1.EntityTurtle;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class InfoBar extends AbstractInfoBar {

  public InfoBar(Entity entity, InfoBarManager infoBarManager) {
    super(entity, infoBarManager);
    this.lines = Lists.newArrayList();
    this.spawnPacketSupplier = Lists.newArrayList();
    this.lineEntityIDs = new IntOpenHashSet();
  }

  private final IntSet lineEntityIDs;
  private final ArrayList<Supplier<DoubleLinkedPacketHost>> spawnPacketSupplier;
  private final ArrayList<LineEntity> lines;

  @Override
  protected void showTo(Player player) {
    this.viewingPlayer.add(player);
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    for (int index = 0; index < spawnPacketSupplier.size(); index++) {
      DoubleLinkedPacketHost packet = this.spawnPacketSupplier.get(index).get();
      if (packet.type == LinkedPacketType.NMS_PACKET) {
        packet.sendNMS(connection);
      } else {
        packet.sendProtocol(player);
      }
    }
  }

  @Override
  protected void hideFrom(Player player) {
    this.viewingPlayer.remove(player);
    int index = 0;
    int[] ids = new int[this.lineEntityIDs.size()];
    for (int id : this.lineEntityIDs) {
      ids[index++] = id;
    }
    PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(ids);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroyPacket);
  }

  @Override
  public void editLine(int index, Function<String, String> lineEdit) {
    LineEntity line = this.lines.get(index);
    DoubleLinkedPacketHost packet = line.setNameAndGetMeta(lineEdit.apply(line.currentLine));
    for (Player player : this.viewingPlayer) {
      packet.sendNMS(((CraftPlayer) player).getHandle().playerConnection);
    }
  }

  @Override
  public void setLine(int index, String newLine) {
    LineEntity line = this.lines.get(index);
    DoubleLinkedPacketHost packet = line.setNameAndGetMeta(newLine);
    for (Player player : this.viewingPlayer) {
      packet.sendNMS(((CraftPlayer) player).getHandle().playerConnection);
    }
  }

  @Override
  public void addLine(String newLine, InfoLineSpacing spacing) {
    LinePart spacingPart;

    switch (spacing) {
      case LARGE:
        spacingPart = new LargeSpacingEntity(this.entity.getLocation());
        break;
      case MEDIUM:
        spacingPart = new MediumSpacingEntity(this.entity.getLocation());
        break;
      case SMALL:
        spacingPart = new SmallSpacingEntity(this.entity.getLocation());
        break;
      default:
        spacingPart = new LargeSpacingEntity(this.entity.getLocation());
        break;
    }

    LinePart lineEntity = new LineEntity(this.entity.getLocation(), newLine);

    int lineID = lineEntity.getHandle().getId();
    int spacingID = spacingPart.getHandle().getId();

    this.lineEntityIDs.add(lineID);
    this.lineEntityIDs.add(spacingID);

    this.infoBarManager.addMapping(lineID, this.getEntity());
    this.infoBarManager.addMapping(spacingID, this.getEntity());

    ArrayList<Supplier<DoubleLinkedPacketHost>> newPackets = Lists.newArrayList();

    net.minecraft.server.v1_14_R1.Entity hostEntity =
        this.lines.size() == 0 ? ((CraftEntity) this.entity).getHandle()
            : this.lines.get(lines.size() - 1);

    newPackets.add(() -> spacingPart.getLivingPacket());
    newPackets.add(() -> getMountPacket(hostEntity, spacingPart.getHandle()));
    newPackets.add(() -> spacingPart.getMetaPacket());

    newPackets.add(() -> lineEntity.getSpawnPacket());
    newPackets.add(() -> spacingPart.getMountPacket(lineEntity.getHandle().getId()));
    newPackets.add(() -> spacingPart.getMetaPacket());
    newPackets.add(() -> lineEntity.getMetaPacket());

    this.lines.add((LineEntity) lineEntity);

    Map<Player, PlayerConnection> connections = Maps.newHashMap();

    for (Player player : this.viewingPlayer) {
      connections.put(player, ((CraftPlayer) player).getHandle().playerConnection);
    }

    for (int i = 0; i < newPackets.size(); i++) {
      Supplier<DoubleLinkedPacketHost> packetSupplier = newPackets.get(i);
      this.spawnPacketSupplier.add(packetSupplier);
      DoubleLinkedPacketHost packet = packetSupplier.get();
      if (packet.type == LinkedPacketType.NMS_PACKET) {
        for (PlayerConnection conn : connections.values()) {
          packet.sendNMS(conn);
        }
      } else {
        for (Player player : connections.keySet()) {
          packet.sendProtocol(player);
        }
      }

    }
  }

  private DoubleLinkedPacketHost getMountPacket(net.minecraft.server.v1_14_R1.Entity mount,
      net.minecraft.server.v1_14_R1.Entity rider) {
    WrapperPlayServerMount packet = new WrapperPlayServerMount();
    packet.setEntityID(mount.getId());
    packet.setPassengerIds(new int[]{rider.getId()});

    return DoubleLinkedPacketHost.of(packet.getHandle());
  }

  @Override
  public int getSize() {
    return this.lines.size();
  }

  static interface LinePart {

    public net.minecraft.server.v1_14_R1.Entity getHandle();

    public DoubleLinkedPacketHost getSpawnPacket();

    public DoubleLinkedPacketHost getMetaPacket();

    public DoubleLinkedPacketHost setNameAndGetMeta(String line);

    public DoubleLinkedPacketHost getMountPacket(int riderID);

    public DoubleLinkedPacketHost getLivingPacket();
  }

  private static final class LineEntity extends EntityArmorStand implements LinePart {

    public LineEntity(Location location, String line) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(),
          location.getZ());
      this.setMarker(true);
      this.setInvisible(true);
      this.setCustomName(new ChatMessage(line));
      this.setCustomNameVisible(true);
      this.currentLine = line;
    }

    private void setCurrentLine(String line) {
      this.setCustomName(new ChatMessage(line));
      this.currentLine = line;
    }

    private String currentLine;

    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.datawatcher, true));
    }

    public DoubleLinkedPacketHost setNameAndGetMeta(String line) {
      this.setCurrentLine(line);
      return this.getMetaPacket();
    }

    public DoubleLinkedPacketHost getMountPacket(int riderID) {
      WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.server.v1_14_R1.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return null;
    }

  }

  private static final class SmallSpacingEntity extends EntityTurtle implements LinePart {

    public SmallSpacingEntity(Location location) {
      super(EntityTypes.TURTLE, ((CraftWorld) location.getWorld()).getHandle());
      this.setInvisible(true);
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setAge(-100);
      this.ageLocked = true;
    }


    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.datawatcher, true));
    }

    public DoubleLinkedPacketHost setNameAndGetMeta(String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    public DoubleLinkedPacketHost getMountPacket(int riderID) {
      WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.server.v1_14_R1.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  private static final class MediumSpacingEntity extends EntityRabbit implements LinePart {

    public MediumSpacingEntity(Location location) {
      super(EntityTypes.RABBIT, ((CraftWorld) location.getWorld()).getHandle());
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setInvisible(true);
      this.setAge(-100);
      this.ageLocked = true;
    }


    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.datawatcher, true));
    }

    public DoubleLinkedPacketHost setNameAndGetMeta(String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    public DoubleLinkedPacketHost getMountPacket(int riderID) {
      WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.server.v1_14_R1.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  private static final class LargeSpacingEntity extends EntityPig implements LinePart {

    public LargeSpacingEntity(Location location) {
      super(EntityTypes.PIG, ((CraftWorld) location.getWorld()).getHandle());
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setInvisible(true);
      this.setAge(-100);
      this.ageLocked = true;
    }


    public DoubleLinkedPacketHost getSpawnPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntity(this));
    }

    public DoubleLinkedPacketHost getMetaPacket() {
      return DoubleLinkedPacketHost
          .of(new PacketPlayOutEntityMetadata(this.getId(), this.datawatcher, true));
    }

    public DoubleLinkedPacketHost setNameAndGetMeta(String line) {
      this.setCustomName(new ChatMessage(line));
      return this.getMetaPacket();
    }

    public DoubleLinkedPacketHost getMountPacket(int riderID) {
      WrapperPlayServerMount packet = new WrapperPlayServerMount();
      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{riderID});

      return DoubleLinkedPacketHost.of(packet.getHandle());
    }

    @Override
    public net.minecraft.server.v1_14_R1.Entity getHandle() {
      return this;
    }

    @Override
    public DoubleLinkedPacketHost getLivingPacket() {
      return DoubleLinkedPacketHost.of(new PacketPlayOutSpawnEntityLiving(this));
    }

  }

  @Override
  public boolean isInLineOfSight(Player player) {
    return player.hasLineOfSight(this.entity);
  }

}