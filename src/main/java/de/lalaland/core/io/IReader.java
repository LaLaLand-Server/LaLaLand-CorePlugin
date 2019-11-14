package de.lalaland.core.io;

public interface IReader {
	
	public abstract Object read(final Class<?> classToDeserialize, final Object defaultValue);
	
}
