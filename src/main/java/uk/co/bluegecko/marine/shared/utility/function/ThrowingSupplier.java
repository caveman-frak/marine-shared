package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {

	T get() throws E;

}