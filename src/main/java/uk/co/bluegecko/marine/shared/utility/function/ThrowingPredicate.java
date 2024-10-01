package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingPredicate<T, E extends Exception> {

	boolean test(T t) throws E;

}