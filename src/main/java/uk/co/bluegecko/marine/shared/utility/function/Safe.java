package uk.co.bluegecko.marine.shared.utility.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Create wrappers for common functions that will fail fast if given null arguments.
 */
public interface Safe {

	static <T> Predicate<T> equal(T comparison) {
		return v -> (comparison == null && v == null) || ((comparison != null && v != null) && comparison.equals(v));
	}

	static <T> Predicate<T> safe(Predicate<T> predicate) {
		return v -> v != null && predicate.test(v);
	}

	static <T, S> Function<T, S> safe(Function<T, S> function) {
		return v -> v == null ? null : function.apply(v);
	}

	static <T> UnaryOperator<T> safe(UnaryOperator<T> operator) {
		return v -> v == null ? null : operator.apply(v);
	}

	static <T, S> BiPredicate<T, S> safe(BiPredicate<T, S> predicate) {
		return (t, s) -> t != null && s != null && predicate.test(t, s);
	}

	static <T, S, R> BiFunction<T, S, R> safe(BiFunction<T, S, R> function) {
		return (t, s) -> t == null || s == null ? null : function.apply(t, s);
	}

	static <T> BinaryOperator<T> safe(BinaryOperator<T> function) {
		return (t, s) -> t == null || s == null ? null : function.apply(t, s);
	}

}