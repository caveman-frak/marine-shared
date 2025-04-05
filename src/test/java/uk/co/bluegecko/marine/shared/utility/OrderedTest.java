package uk.co.bluegecko.marine.shared.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class OrderedTest {

	@Test
	void lookupSuccess() {
		assertThat(Foo.fromId(new UUID(0, 10))).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFailure() {
		assertThat(Foo.fromId(new UUID(0, 20))).isEmpty();
	}

	@Test
	void compareLess() {
		assertThat(Foo.THAT.compareTo(Foo.THIS)).isEqualTo(-1);
	}

	@Test
	void compareGreater() {
		assertThat(Foo.THIS.compareTo(Foo.THAT)).isEqualTo(1);
	}

	private record Foo(UUID id) implements Fluent.Ordered<Foo, UUID> {

		public static final Foo THIS = new Foo(new UUID(0, 10));
		public static final Foo THAT = new Foo(new UUID(0, 5));

		public static Optional<Foo> fromId(UUID id) {
			return Identified.fromId(Stream.of(Foo.THIS, Foo.THAT), id);
		}

	}

}