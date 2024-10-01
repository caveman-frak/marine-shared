package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingUnaryOperator<T, E extends Exception> {

	T apply(T t) throws E;

}