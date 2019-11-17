package de.lalaland.core.utils.holograms;

import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IHologramFactory {
	
	public AbstractHologram supplyHologram(Location location, Predicate<Player> viewFilter, AbstractHologramManager manager);
	
}
