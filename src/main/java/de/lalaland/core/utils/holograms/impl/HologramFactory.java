package de.lalaland.core.utils.holograms.impl;

import de.lalaland.core.utils.holograms.AbstractHologram;
import de.lalaland.core.utils.holograms.AbstractHologramManager;
import de.lalaland.core.utils.holograms.IHologramFactory;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramFactory implements IHologramFactory {

  @Override
  public AbstractHologram supplyHologram(Location location, Predicate<Player> viewFilter,
      AbstractHologramManager manager) {
    return new Hologram(location, viewFilter, manager);
  }
}
