package uk.co.bluegecko.marine.shared.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.marine.shared.utility.Sort.any;
import static uk.co.bluegecko.marine.shared.utility.Sort.compare;
import static uk.co.bluegecko.marine.shared.utility.Sort.entry;
import static uk.co.bluegecko.marine.shared.utility.Sort.nullsFirst;
import static uk.co.bluegecko.marine.shared.utility.Sort.nullsLast;
import static uk.co.bluegecko.marine.shared.utility.Sort.reverse;
import static uk.co.bluegecko.marine.shared.utility.Sort.sort;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Value;
import org.junit.jupiter.api.Test;

class SortTest {

	private static final Foo NULL = null;
	private static final Foo ONE = new Foo(1);
	private static final Foo TWO = new Foo(2);
	private static final Foo THREE = new Foo(3);
	private static final Foo FOUR = new Foo(4);

	@Test
	void incomparableFoo() {
		assertThat(compare(ONE, ONE)).describedAs("1==1").isEqualTo(0);
		assertThat(compare(ONE, TWO)).describedAs("1<2").isEqualTo(-1);
		assertThat(compare(TWO, ONE)).describedAs("2>1").isEqualTo(1);
	}

	@Test
	void comparableNumber() {
		assertThat(compare(1, 1)).describedAs("1==1").isEqualTo(0);
		assertThat(compare(1, 2)).describedAs("1<2").isEqualTo(-1);
		assertThat(compare(2, 1)).describedAs("2>1").isEqualTo(1);
	}

	@Test
	void comparableString() {
		assertThat(compare("1", "1")).describedAs("1==1").isEqualTo(0);
		assertThat(compare("1", "2")).describedAs("1<2").isEqualTo(-1);
		assertThat(compare("2", "1")).describedAs("2>1").isEqualTo(1);
	}

	@Test
	void compareFooValue() {
		Comparator<Foo> comparator = any(Foo::getValue);
		Foo one = new Foo(1);
		assertThat(ONE.equals(one)).isTrue();
		assertThat(comparator.compare(ONE, one)).describedAs("1==1").isEqualTo(0);
		assertThat(comparator.compare(ONE, TWO)).describedAs("1<2").isEqualTo(-1);
		assertThat(comparator.compare(TWO, ONE)).describedAs("2>1").isEqualTo(1);
	}

	@Test
	void sortedFoo() {
		Collection<Foo> c = Set.of(FOUR, TWO, THREE, ONE);
		assertThat(c.stream().sorted(any())).containsSequence(ONE, TWO, THREE, FOUR);
	}

	@Test
	void reverseSortedFoo() {
		Collection<Foo> c = Set.of(FOUR, TWO, THREE, ONE);
		assertThat(c.stream().sorted(reverse(any()))).containsSequence(FOUR, THREE, TWO, ONE);
	}

	@Test
	void sortedFooNullFirst() {
		Collection<Foo> c = Stream.of(FOUR, TWO, NULL, THREE, ONE).toList();
		assertThat(c.stream().sorted(nullsFirst(any()))).containsSequence(NULL, ONE, TWO, THREE, FOUR);
	}

	@Test
	void sortedFooNullLast() {
		Collection<Foo> c = Stream.of(FOUR, TWO, NULL, THREE, ONE).toList();
		assertThat(c.stream().sorted(nullsLast(any()))).containsSequence(ONE, TWO, THREE, FOUR, NULL);
	}

	@Test
	void sortedMapFoo() {
		Map<Foo, Integer> m = Map.of(FOUR, 4, TWO, 2, THREE, 3, ONE, 1);
		assertThat(m.entrySet().stream().sorted(reverse(entry())).map(Map.Entry::getValue))
				.containsSequence(4, 3, 2, 1);
	}

	@Test
	void sortedMapStream() {
		Map<Foo, Integer> m = Map.of(FOUR, 4, TWO, 2, THREE, 3, ONE, 1);
		assertThat(sort(m).map(Map.Entry::getValue)).containsSequence(1, 2, 3, 4);
	}

	@Value
	private static class Foo {

		int value;

		@Override
		public int hashCode() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Foo that) {
				return value == that.value;
			} else {
				return false;
			}
		}

	}

}