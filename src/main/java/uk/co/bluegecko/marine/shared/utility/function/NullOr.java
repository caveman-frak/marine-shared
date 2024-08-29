package uk.co.bluegecko.marine.shared.utility.function;

import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NullOr<S, T> implements Predicate<S> {

	Function<S, T> extract;
	Predicate<T> predicate;

	/**
	 * Evaluates this predicate on the given argument against the comparison argument.
	 *
	 * @param value the input argument.
	 * @return {@code true} if either the comparison or value are null, or they are equal, otherwise {@code false}.
	 */
	@Override
	public boolean test(S value) {
		T v = value == null ? null : extract.apply(value);
		return v == null || predicate.test(v);
	}

	public static <S, T> NullOr<S, T> nullOr(Function<S, T> extract, Predicate<T> predicate) {
		return new NullOr<>(extract, predicate);
	}

	public static <T> NullOr<T, T> nullOr(Predicate<T> predicate) {
		return new NullOr<>(Function.identity(), predicate);
	}

}