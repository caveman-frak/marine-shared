package uk.co.bluegecko.marine.shared.utility.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

class OrderedTest {

	@Test
	void lookupSuccess() {
		assertThat(Foo.fromId(new UUID(0, 1))).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFailure() {
		assertThat(Foo.fromId(new UUID(0, 10))).isEmpty();
	}

	@Test
	void compareLess() {
		assertThat(Foo.THIS.compareTo(Foo.THAT)).isEqualTo(-1);
	}

	@Test
	void compareGreater() {
		assertThat(Foo.THAT.compareTo(Foo.THIS)).isEqualTo(1);
	}

	@Test
	void compareEqual() {
		assertThat(Foo.THIS.compareTo(Foo.THIS)).isEqualTo(0);
	}

	@RequiredArgsConstructor
	@Getter
	private enum Foo implements Ordered<Foo, UUID> {

		THIS(new UUID(0, 1)),
		THAT(new UUID(0, 2));

		private final UUID id;

		public static Optional<Foo> fromId(UUID id) {
			return Identified.fromId(Foo.values(), id);
		}

	}

}