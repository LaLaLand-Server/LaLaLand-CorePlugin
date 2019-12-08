package de.lalaland.core.modules.mobs.custommobs.zombie;

import de.lalaland.core.modules.mobs.custommobs.ComplexModelFactory;
import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SingleTestFactory implements ComplexModelFactory<TestSingleZombie> {

  @Override
  public TestSingleZombie spawn(final Location location, final MobModelManager mobModelManager) {
    final World world = ((CraftWorld) location.getWorld()).getHandle();

    final ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    as.setVisible(false);
    as.setGravity(false);
    as.setMarker(true);

    final TestSingleZombie mob = new TestSingleZombie(world.getMinecraftWorld(), mobModelManager, ((CraftArmorStand) as).getHandle());
    world.addEntity(mob);
    mob.getBukkitLivingEntity().teleport(location);
    mob.getBukkitLivingEntity().addPassenger(as);
    mob.equipNormal();

    return mob;
  }

}
