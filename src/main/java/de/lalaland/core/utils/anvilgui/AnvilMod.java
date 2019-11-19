package de.lalaland.core.utils.anvilgui;

import de.lalaland.core.utils.items.ItemBuilder;
import java.lang.reflect.Field;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.ChatComponentText;
import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.Container;
import net.minecraft.server.v1_14_R1.ContainerAccess;
import net.minecraft.server.v1_14_R1.ContainerAnvil;
import net.minecraft.server.v1_14_R1.Containers;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 19.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AnvilMod extends NMSMod implements AnvilImplementation {

  public int getNextContainerId(Player player) {
    return toNMS(player).nextContainerCounter();
  }

  public void handleInventoryCloseEvent(Player player) {
    CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
  }

  public void sendPacketOpenWindow(Player player, int containerId) {
    toNMS(player).playerConnection.sendPacket(
        new PacketPlayOutOpenWindow(containerId, Containers.ANVIL, new ChatMessage("Anvil")));
  }

  public void sendPacketCloseWindow(Player player, int containerId) {
    toNMS(player).playerConnection.sendPacket(new PacketPlayOutCloseWindow(containerId));
  }

  public void setActiveContainerDefault(Player player) {
    toNMS(player).activeContainer = toNMS(player).defaultContainer;
  }

  public void setActiveContainer(Player player, Object container) {
    toNMS(player).activeContainer = (Container) container;
  }

  public void setActiveContainerId(Object container, int containerId) {
    Field id = null;
    try {

      id = Container.class.getField("windowId");
      id.setAccessible(true);
      id.setInt(container, containerId);

    } catch (SecurityException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void addActiveContainerSlotListener(Object container, Player player) {
    ((Container) container).addSlotListener(toNMS(player));
  }

  public Inventory toBukkitInventory(Object container) {
    return ((Container) container).getBukkitView().getTopInventory();
  }

  public Object newContainerAnvil(Player player) {
    return new AnvilContainer(toNMS(player));
  }

  /**
   * Turns a {@link Player} into an NMS one
   *
   * @param player The player to be converted
   * @return the NMS EntityPlayer
   */
  private EntityPlayer toNMS(Player player) {
    return ((CraftPlayer) player).getHandle();
  }

  /**
   * Modifications to ContainerAnvil that makes it so you don't have to have xp to use this anvil
   */
  private class AnvilContainer extends ContainerAnvil {

    public AnvilContainer(EntityHuman entityhuman) {
      super(getNextContainerId((Player) entityhuman.getBukkitEntity()), entityhuman.inventory,
          ContainerAccess
              .at(entityhuman.world, new BlockPosition(0, 0, 0)));
      this.checkReachable = false;
      setTitle(new ChatMessage("Repair & Name"));

      output = CraftItemStack.asNMSCopy(new ItemBuilder(Material.COAL).modelData(1000).build());
      // this.getSlot(1).set(CraftItemStack.asNMSCopy(new
      // ItemBuilderWrapper(Material.COAL).setModelData(5000).build()));
      this.getSlot(1).set(CraftItemStack.asNMSCopy(new ItemBuilder(Material.COAL).build()));
    }

    private final ItemStack output;

    @Override
    public void e() {
      this.levelCost.set(0);
      if (this.renameText != null && !this.renameText.isEmpty()) {
        this.output
            .a(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', this.renameText)));
        this.getSlot(2).set(this.output);
      } else {
        return;
      }
      CraftEventFactory.callPrepareAnvilEvent(this.getBukkitView(), this.output);
      this.c();
    }
  }

}
