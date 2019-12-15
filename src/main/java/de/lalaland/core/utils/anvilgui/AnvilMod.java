package de.lalaland.core.utils.anvilgui;

import de.lalaland.core.utils.items.ItemBuilder;
import java.lang.reflect.Field;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.Container;
import net.minecraft.server.v1_15_R1.ContainerAccess;
import net.minecraft.server.v1_15_R1.ContainerAnvil;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
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

  @Override
  public int getNextContainerId(final Player player) {
    return toNMS(player).nextContainerCounter();
  }

  @Override
  public void handleInventoryCloseEvent(final Player player) {
    CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
  }

  @Override
  public void sendPacketOpenWindow(final Player player, final int containerId) {
    toNMS(player).playerConnection.sendPacket(
        new PacketPlayOutOpenWindow(containerId, Containers.ANVIL, new ChatMessage("Anvil")));
  }

  @Override
  public void sendPacketCloseWindow(final Player player, final int containerId) {
    toNMS(player).playerConnection.sendPacket(new PacketPlayOutCloseWindow(containerId));
  }

  @Override
  public void setActiveContainerDefault(final Player player) {
    toNMS(player).activeContainer = toNMS(player).defaultContainer;
  }

  @Override
  public void setActiveContainer(final Player player, final Object container) {
    toNMS(player).activeContainer = (Container) container;
  }

  @Override
  public void setActiveContainerId(final Object container, final int containerId) {
    Field id = null;
    try {

      id = Container.class.getField("windowId");
      id.setAccessible(true);
      id.setInt(container, containerId);

    } catch (final SecurityException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addActiveContainerSlotListener(final Object container, final Player player) {
    ((Container) container).addSlotListener(toNMS(player));
  }

  @Override
  public Inventory toBukkitInventory(final Object container) {
    return ((Container) container).getBukkitView().getTopInventory();
  }

  @Override
  public Object newContainerAnvil(final Player player) {
    return new AnvilContainer(toNMS(player));
  }

  /**
   * Turns a {@link Player} into an NMS one
   *
   * @param player The player to be converted
   * @return the NMS EntityPlayer
   */
  private EntityPlayer toNMS(final Player player) {
    return ((CraftPlayer) player).getHandle();
  }

  /**
   * Modifications to ContainerAnvil that makes it so you don't have to have xp to use this anvil
   */
  private class AnvilContainer extends ContainerAnvil {

    public AnvilContainer(final EntityHuman entityhuman) {
      super(getNextContainerId((Player) entityhuman.getBukkitEntity()), entityhuman.inventory,
          ContainerAccess
              .at(entityhuman.world, new BlockPosition(0, 0, 0)));
      checkReachable = false;
      setTitle(new ChatMessage("Repair & Name"));

      output = CraftItemStack.asNMSCopy(new ItemBuilder(Material.COAL).modelData(1000).build());
      // this.getSlot(1).set(CraftItemStack.asNMSCopy(new
      // ItemBuilderWrapper(Material.COAL).setModelData(5000).build()));
      getSlot(1).set(CraftItemStack.asNMSCopy(new ItemBuilder(Material.COAL).build()));
    }

    private final ItemStack output;

    @Override
    public void e() {
      levelCost.set(0);
      if (renameText != null && !renameText.isEmpty()) {
        output
            .a(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', renameText)));
        getSlot(2).set(output);
      } else {
        return;
      }
      CraftEventFactory.callPrepareAnvilEvent(getBukkitView(), output);
      c();
    }
  }

}
