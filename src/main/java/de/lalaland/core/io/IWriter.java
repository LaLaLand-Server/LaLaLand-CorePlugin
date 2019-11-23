package de.lalaland.core.io;

public interface IWriter {
	
	public abstract <T> void write(final T dataObject);
	
}
