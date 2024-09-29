package uk.co.bluegecko.marine.shared.utility.stream;

import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectToIndexedMap<T> implements Collector<T, SortedMap<Integer, T>, SortedMap<Integer, T>> {

	private final AtomicInteger index;

	/**
	 * A function that creates and returns a new mutable result container.
	 *
	 * @return a function which returns a new, mutable result container
	 */
	@Override
	public Supplier<SortedMap<Integer, T>> supplier() {
		return TreeMap::new;
	}

	/**
	 * A function that folds a value into a mutable result container.
	 *
	 * @return a function which folds a value into a mutable result container
	 */
	@Override
	public BiConsumer<SortedMap<Integer, T>, T> accumulator() {
		return (a, v) -> a.put(index.getAndIncrement(), v);
	}

	/**
	 * A function that accepts two partial results and merges them.  The combiner function may fold state from one
	 * argument into the other and return that, or may return a new result container.
	 *
	 * @return a function which combines two partial results into a combined result
	 */
	@Override
	public BinaryOperator<SortedMap<Integer, T>> combiner() {
		return (a, b) -> {
			a.putAll(b);
			return a;
		};
	}

	/**
	 * Perform the final transformation from the intermediate accumulation type {@code A} to the final result type
	 * {@code R}.
	 *
	 * <p>If the characteristic {@code IDENTITY_FINISH} is
	 * set, this function may be presumed to be an identity transform with an unchecked cast from {@code A} to
	 * {@code R}.
	 *
	 * @return a function which transforms the intermediate result to the final result
	 */
	@Override
	public Function<SortedMap<Integer, T>, SortedMap<Integer, T>> finisher() {
		return Collections::unmodifiableSortedMap;
	}

	/**
	 * Returns a {@code Set} of {@code Collector.Characteristics} indicating the characteristics of this Collector. This
	 * set should be immutable.
	 *
	 * @return an immutable set of collector characteristics
	 */
	@Override
	public Set<Characteristics> characteristics() {
		return Set.of();
	}

	public static <T> CollectToIndexedMap<T> toIdxdMap() {
		return new CollectToIndexedMap<>(new AtomicInteger());
	}

}