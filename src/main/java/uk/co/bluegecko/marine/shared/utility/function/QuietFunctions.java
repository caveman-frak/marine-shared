package uk.co.bluegecko.marine.shared.utility.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class QuietFunctions {

	private QuietFunctions() {
	}

	public static <T, E extends Exception> Consumer<T> quietConsumer(ThrowingConsumer<T, E> consumer) {
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

	public static <T, U, E extends Exception> BiConsumer<T, U> quietConsumer(ThrowingBiConsumer<T, U, E> consumer) {
		return (t, u) -> {
			try {
				consumer.accept(t, u);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, R, E extends Exception> Function<T, R> quietFunction(ThrowingFunction<T, R, E> function) {
		return (t) -> {
			try {
				return function.apply(t);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, U, R, E extends Exception> BiFunction<T, U, R> quietFunction(
			ThrowingBiFunction<T, U, R, E> function) {
		return (t, u) -> {
			try {
				return function.apply(t, u);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, E extends Exception> Predicate<T> quietPredicate(ThrowingPredicate<T, E> predicate) {
		return (t) -> {
			try {
				return predicate.test(t);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, U, E extends Exception> BiPredicate<T, U> quietPredicate(ThrowingBiPredicate<T, U, E> predicate) {
		return (t, u) -> {
			try {
				return predicate.test(t, u);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, E extends Exception> Supplier<T> quietSupplier(ThrowingSupplier<T, E> supplier) {
		return () -> {
			try {
				return supplier.get();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, E extends Exception> UnaryOperator<T> quietOperator(ThrowingUnaryOperator<T, E> operator) {
		return (t) -> {
			try {
				return operator.apply(t);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

	public static <T, E extends Exception> BinaryOperator<T> quietOperator(
			ThrowingBinaryOperator<T, E> operator) {
		return (t1, t2) -> {
			try {
				return operator.apply(t1, t2);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

}