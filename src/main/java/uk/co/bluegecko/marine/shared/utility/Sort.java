package uk.co.bluegecko.marine.shared.utility;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/**
 * Sort / {@link Comparator} functions that allow processing of any type of object using {@link Object#hashCode()} as
 * the basis for comparison if the object does not support {@link Comparable}.
 * <p/>
 * Primary use-case is for ensuring consistent ordering when testing arrays and collections without having to explicitly
 * declare everything as implementing comparable.
 */
public final class Sort {

	private Sort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Obtain a Comparator using the passed extract function and the compare function.
	 *
	 * @param extractFn extract the element to be compared.
	 * @param compareFn the fallback comparator value.
	 * @param <E>       the base type.
	 * @param <T>       the type to be compared.
	 * @return the comparator for the element.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <E, T> Comparator<E> element(Function<E, T> extractFn, ToIntFunction<T> compareFn) {
		return (a, b) -> {
			T a1 = extractFn.apply(a);
			T b1 = extractFn.apply(b);
			if (a1 instanceof Comparable c) {
				return c.compareTo(b1);
			} else if (a.equals(b1)) {
				return 0;
			} else {
				return Integer.compare(compareFn.applyAsInt(a1), compareFn.applyAsInt(b1));
			}
		};
	}

	/**
	 * Obtain a Comparator using the passed extract function, fallback compare is hashcode.
	 *
	 * @param extractFn extract the element to be compared.
	 * @param <E>       the base type.
	 * @param <T>       the type to be compared.
	 * @return the comparator for the element.
	 */
	public static <E, T> Comparator<E> element(Function<E, T> extractFn) {
		return element(extractFn, Object::hashCode);
	}

	/**
	 * Obtain a Comparator using the fallback compare method.
	 *
	 * @param <T> the type to be compared.
	 * @return the comparator for the object.
	 */
	public static <T> Comparator<T> any(ToIntFunction<T> compareFn) {
		return element(Function.identity(), compareFn);
	}

	/**
	 * Obtain a Comparator with fallback compare using hashcode.
	 *
	 * @param <T> the type to be compared.
	 * @return the comparator for the object.
	 */
	public static <T> Comparator<T> any() {
		return any(Object::hashCode);
	}

	/**
	 * Reverse the passed Comparator.
	 *
	 * @param comparator the base comparator.
	 * @param <T>        the type to be compared.
	 * @return the modified comparator.
	 */
	public static <T> Comparator<T> reverse(Comparator<T> comparator) {
		return (a, b) -> comparator.compare(a, b) * -1;
	}

	/**
	 * Order nulls first for the passed Comparator.
	 *
	 * @param comparator the base comparator.
	 * @param <T>        the type to be compared.
	 * @return the modified comparator.
	 */
	public static <T> Comparator<T> nullsFirst(Comparator<T> comparator) {
		return (a, b) -> a == null ? -1 : b == null ? 1 : comparator.compare(a, b);
	}

	/**
	 * Order nulls last for the passed Comparator.
	 *
	 * @param comparator the base comparator.
	 * @param <T>        the type to be compared.
	 * @return the modified comparator.
	 */
	public static <T> Comparator<T> nullsLast(Comparator<T> comparator) {
		return (a, b) -> a == null ? 1 : b == null ? -1 : comparator.compare(a, b);
	}

	/**
	 * Obtain a Comparator for a {@link Map.Entry}.
	 *
	 * @param <K> key type of the entry.
	 * @param <V> value type of the entry.
	 * @return the comparator for the Entry.
	 */
	public static <K, V> Comparator<Map.Entry<K, V>> entry() {
		return element(Map.Entry::getKey);
	}

	/**
	 * Sorted stream from the entry set of the map using the key.
	 *
	 * @param map the map to get the stream for.
	 * @param <K> key type.
	 * @param <V> value type.
	 * @return the sorted stream.
	 */
	public static <K, V> Stream<Map.Entry<K, V>> sort(Map<K, V> map) {
		return map.entrySet().stream().sorted(entry());
	}

	/**
	 * Convenience compare method, with fallback to hashcode.
	 *
	 * @param a   first object to compare.
	 * @param b   second object to compare.
	 * @param <T> type of objects to compare.
	 * @return the compare result.
	 */
	public static <T> int compare(T a, T b) {
		return any().compare(a, b);
	}

}