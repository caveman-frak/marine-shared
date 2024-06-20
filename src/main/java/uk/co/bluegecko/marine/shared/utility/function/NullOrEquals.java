package uk.co.bluegecko.marine.shared.utility.function;

import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Predicate that ignores the parameter if null otherwise does an equals check.
 *
 * @param <T> the type to check.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NullOrEquals<S, T> implements Predicate<S> {

	private final T comparison;
	private final Function<S, T> extract;

	/**
	 * Evaluates this predicate on the given argument against the comparison argument.
	 *
	 * @param value the input argument
	 * @return {@code true} if the comparison is null or the input argument is equal, otherwise {@code false}
	 */
	@Override
	public boolean test(S value) {
		return comparison == null || comparison.equals(extract.apply(value));
	}

	public static <S, T> NullOrEquals<S, T> nullOrEquals(T comparison, Function<S, T> extract) {
		return new NullOrEquals<>(comparison, extract);
	}

	public static <T> NullOrEquals<T, T> nullOrEquals(T comparison) {
		return new NullOrEquals<>(comparison, v -> v);
	}

}