package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Exception> {

	boolean test(T t, U u) throws E;

}