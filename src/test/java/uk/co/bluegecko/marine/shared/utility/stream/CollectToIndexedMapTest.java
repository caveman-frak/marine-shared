package uk.co.bluegecko.marine.shared.utility.stream;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.marine.shared.utility.stream.CollectToIndexedMap.toIdxdMap;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class CollectToIndexedMapTest {

	private static final String[] VALUES =
			{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};

	@Test
	void streamToIdxMap() {
		assertThat(Stream.of(VALUES)
				.collect(toIdxdMap()))
				.hasSize(11)
				.containsExactly(
						entry(0, "Zero"), entry(1, "One"), entry(2, "Two"), entry(3, "Three"),
						entry(4, "Four"), entry(5, "Five"), entry(6, "Six"), entry(7, "Seven"),
						entry(8, "Eight"), entry(9, "Nine"), entry(10, "Ten")
				);
	}

	@Test
	void streamToIdxMapEmpty() {
		assertThat(Stream.of().collect(toIdxdMap())).hasSize(0).isEmpty();
	}

	@Test
	void parallelStreamToIdxMap() {
		assertThat(Stream.of(VALUES)
				.parallel()
				.collect(toIdxdMap()))
				.hasSize(11)
				.containsOnlyKeys(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
				.containsValues(VALUES);
	}

}