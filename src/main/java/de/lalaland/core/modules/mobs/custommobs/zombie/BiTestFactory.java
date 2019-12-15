package de.lalaland.core.modules.mobs.custommobs.zombie;

import de.lalaland.core.modules.mobs.custommobs.ComplexModelFactory;
import de.lalaland.core.modules.mobs.modeledentities.MobModelManager;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 08.12.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BiTestFactory implements ComplexModelFactory<TestBiZombie> {

  @Override
  public TestBiZombie spawn(final Location location, final MobModelManager mobModelManager) {
    final World world = ((CraftWorld) location.getWorld()).getHandle();

    final ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    as.setVisible(false);
    as.setGravity(false);
    as.setMarker(true);

    final TestBiZombie mob = new TestBiZombie(world.getMinecraftWorld(), mobModelManager, ((CraftArmorStand) as).getHandle());
    world.addEntity(mob);
    mob.getBukkitLivingEntity().teleport(location);
    mob.getBukkitLivingEntity().addPassenger(as);
    mob.setInvisible(true);
    mob.getAsBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, false));
    mob.equipNormal();


    return mob;
  }

}
