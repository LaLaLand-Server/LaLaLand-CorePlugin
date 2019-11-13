package de.lalaland.core.utils.functional;

import java.util.function.Function;

public interface TripleFunction <P1, P2, P3, R> {
	
	public abstract R apply(P1 paramOne, P2 paramTwo, P3 paramThree);
	public abstract Function<R, ?> andThen();
	
}
