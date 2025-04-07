package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingBinaryOperator<T, E extends Exception> {

	T apply(T t1, T t2) throws E;

}