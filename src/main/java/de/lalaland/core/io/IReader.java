package de.lalaland.core.io;

public interface IReader {
	
	public abstract <T> T read(final Class<T> classToDeserialize, final T defaultValue);
	
}
