package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {

	R apply(T t) throws E;

}