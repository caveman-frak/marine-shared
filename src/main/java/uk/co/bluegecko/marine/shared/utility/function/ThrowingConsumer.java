package uk.co.bluegecko.marine.shared.utility.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

	void accept(T t) throws E;

	static <T, E extends Exception> Consumer<T> quietConsumer(ThrowingConsumer<T, E> consumer) {
		return (t) -> {
			try {
				consumer.accept(t);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

}