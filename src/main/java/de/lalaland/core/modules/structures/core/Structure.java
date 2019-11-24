package de.lalaland.core.modules.structures.core;

import de.lalaland.core.modules.protection.regions.ProtectedRegion;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of CorePlugin and was created at the 24.11.2019
 *
 * CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Structure {

  public Structure(final ProtectedRegion protectedRegion) {
    this.protectedRegion = protectedRegion;
    validMaterials = new ObjectOpenHashSet<>();
  }

  @Getter
  private final ProtectedRegion protectedRegion;

  private final ObjectSet<Material> validMaterials;

  public boolean isValidMaterial(final Material mat) {
    return validMaterials.contains(mat);
  }

  public Vector getDimension() {
    final BoundingBox box = protectedRegion.getBoundingBoxClone();
    return box.getMax().subtract(box.getMin());
  }

  protected void addValidMaterial(final Material material) {
    validMaterials.add(material);
  }

  public abstract void onBlockBreak(BlockBreakEvent event);

  public abstract void onInteract(PlayerInteractEvent event);

}
