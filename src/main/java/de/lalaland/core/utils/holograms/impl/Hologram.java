package de.lalaland.core.utils.holograms.impl;

import com.google.common.collect.Sets;
import de.lalaland.core.utils.holograms.AbstractHologram;
import de.lalaland.core.utils.holograms.AbstractHologramManager;
import de.lalaland.core.utils.holograms.IHologramLine;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.EntitySlime;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_14_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Hologram extends AbstractHologram {

  public Hologram(Location baseLocation, Predicate<Player> playerFilter,
      AbstractHologramManager manager) {
    super(baseLocation, playerFilter, manager);
    clickableEntitys = Sets.newHashSet();
  }

  private final Set<ClickableEntity> clickableEntitys;

  @Override
  public void appendTextLine(String text) {
    this.appendLine(
        new HologramTextLine(this.getBaseLocation().clone().subtract(0, 0.25 * this.getSize(), 0),
            text, this));
    if (this.clickable && clickableEntitys.size() < this.getSize() / 4) {
      this.fillClickableEntities();
    }
  }

  @Override
  public void appendItemLine(ItemStack item) {
    this.appendLine(
        new HologramItemLine(this.getBaseLocation().clone().subtract(0, 0.25 * this.getSize(), 0),
            item, this));
    if (this.clickable && clickableEntitys.size() < this.getSize() / 4) {
      this.fillClickableEntities();
    }
  }

  private void fillClickableEntities() {
    Set<Player> viewers = this.getViewers();
    for (Player player : viewers) {
      this.hideClickableEntities(player);
    }
    while (clickableEntitys.size() < this.getSize() / 4) {
      this.clickableEntitys.add(new ClickableEntity(
          this.getBaseLocation().clone().subtract(0, clickableEntitys.size() * -1.5, 0)));
    }
    for (Player player : viewers) {
      this.showClickableEntities(player);
    }
    this.registerClickableEntities();
  }

  @Override
  public void setClickable() {
    if (this.clickable) {
      return;
    }
    this.clickable = true;
    this.fillClickableEntities();
  }

  @Override
  protected void showClickableEntities(Player player) {
    for (ClickableEntity clickable : clickableEntitys) {
      clickable.sendSpawnPacket(player);
    }
  }

  @Override
  protected void hideClickableEntities(Player player) {
    for (ClickableEntity clickable : clickableEntitys) {
      clickable.sendDespawnPacket(player);
    }
  }

  private final class ClickableEntity extends EntitySlime {

    public ClickableEntity(Location location) {
      super(EntityTypes.SLIME, ((CraftWorld) location.getWorld()).getHandle());
      this.setPosition(location.getX(), location.getY(), location.getZ());
      this.setInvisible(true);
      this.setSize(2, true);
      this.setInvulnerable(true);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
      return false;
    }

    @Override
    protected boolean damageEntity0(DamageSource damagesource, float f) {
      return false;
    }


    public void sendSpawnPacket(Player player) {
      ((CraftPlayer) player).getHandle().playerConnection
          .sendPacket(new PacketPlayOutSpawnEntityLiving(this));
    }

    public void sendDespawnPacket(Player player) {
      ((CraftPlayer) player).getHandle().playerConnection
          .sendPacket(new PacketPlayOutEntityDestroy(this.getId()));
    }
  }

  @Override
  protected Set<Integer> getClickableEntityIds() {
    return this.clickableEntitys.stream().map(e -> e.getId()).collect(Collectors.toSet());
  }

  @Override
  protected void moveHologram(Vector direction) {
    for (Player viewer : this.getViewers()) {
      for (IHologramLine<?> line : this.lines) {
        line.sendMove(viewer, direction);
      }
    }
  }
}
