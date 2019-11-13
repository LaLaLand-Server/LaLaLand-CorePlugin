package de.lalaland.core.utils.nbtapi.nmsitemutils.nmsmappings;

/**
 * Package enum
 * 
 * @author tr7zw
 *
 */
public enum PackageWrapper {
	NMS("net.minecraft.server"),
	CRAFTBUKKIT("org.bukkit.craftbukkit"),
	;
	
	private final String uri;

	private PackageWrapper(String uri) {
		this.uri = uri;
	}

	/**
	 * @return The Uri for that package
	 */
	public String getUri() {
		return uri;
	}

}
