package uk.co.bluegecko.marine.shared.utility.function;

import static uk.co.bluegecko.marine.shared.utility.function.Safe.safe;

import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Predicate that returns true if the parameter if null otherwise tests the passed predicate.
 *
 * @param <S> the base type to check.
 * @param <T> the extracted type to check.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NullOr<S, T> implements Predicate<S> {

	Function<S, T> extract;
	Predicate<T> predicate;

	/**
	 * Evaluates this predicate against the given argument.
	 *
	 * @param value the input argument.
	 * @return {@code true} if the base or extracted value are null, or the predicate is true, otherwise {@code false}.
	 */
	@Override
	public boolean test(S value) {
		T v = safe(extract).apply(value);
		return nullIsTrue(predicate).test(v);
	}

	public static <T> Predicate<T> nullIsTrue(Predicate<T> predicate) {
		return v -> v == null || predicate.test(v);
	}

	public static <S, T> Predicate<S> nullOr(Function<S, T> extract, Predicate<T> predicate) {
		return new NullOr<>(extract, predicate);
	}

	public static <T> Predicate<T> nullOr(Predicate<T> predicate) {
		return new NullOr<>(Function.identity(), predicate);
	}

	public static <T> Predicate<T> nullIsEqual(T comparison) {
		return v -> comparison == null || v == null || comparison.equals(v);
	}

	public static <S, T> Predicate<S> nullOrEquals(Function<S, T> extract, T comparison) {
		return nullOr(extract, nullIsEqual(comparison));
	}

	public static <T> Predicate<T> nullOrEquals(T comparison) {
		return nullOrEquals(Function.identity(), comparison);
	}

}