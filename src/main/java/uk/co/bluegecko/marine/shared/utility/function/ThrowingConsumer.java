package uk.co.bluegecko.marine.shared.utility.function;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

	void accept(T t) throws E;

}