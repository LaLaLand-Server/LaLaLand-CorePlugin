package de.lalaland.core.utils.holograms.impl.infobar;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PlayerConnection;

public class DoubleLinkedPacketHost {
	
	public static DoubleLinkedPacketHost of(Packet<?> packet) {
		return new DoubleLinkedPacketHost(packet);
	}
	
	public static DoubleLinkedPacketHost of(PacketContainer packet) {
		return new DoubleLinkedPacketHost(packet);
	}
	
	private DoubleLinkedPacketHost(Packet<?> packet) {
		this.NMSPacket = packet;
		this.protocolPacket = null;
		this.protManager = null;
		this.type = LinkedPacketType.NMS_PACKET;
	}
	
	private DoubleLinkedPacketHost(PacketContainer packet) {
		this.NMSPacket = null;
		this.protocolPacket = packet;
		this.protManager = ProtocolLibrary.getProtocolManager();
		this.type = LinkedPacketType.PROTOCOL_PACKET;
	}
	
	private final ProtocolManager protManager;
	public final LinkedPacketType type;
	private final Packet<?> NMSPacket;
	private final PacketContainer protocolPacket;
	
	public void sendNMS(PlayerConnection conn) {
		Preconditions.checkState(this.type == LinkedPacketType.NMS_PACKET);
		conn.sendPacket(this.NMSPacket);
	}
	
	public void sendProtocol(Player player) {
		Preconditions.checkState(this.type == LinkedPacketType.PROTOCOL_PACKET);
		try {
			protManager.sendServerPacket(player, this.protocolPacket);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static enum LinkedPacketType{
		NMS_PACKET,
		PROTOCOL_PACKET;
	}
	
}
