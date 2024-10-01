package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {

	void accept(T t, U u) throws E;

}