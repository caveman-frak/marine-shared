package uk.co.bluegecko.marine.shared.utility.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.shared.utility.Fluent;

class IdentifiedTest {

	@Test
	void lookupFooSuccess() {
		assertThat(Foo.fromId(10)).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFooFailure() {
		assertThat(Foo.fromId(20)).isEmpty();
	}

	@Test
	void lookupFooWithOffsetSuccess() {
		assertThat(Foo.fromOffset(10)).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFooWithOffsetFailure() {
		assertThat(Foo.fromOffset(20)).isEmpty();
	}

	@Test
	void lookupBarSuccess() {
		assertThat(Bar.fromId(new UUID(0, 1))).isPresent().get().isEqualTo(Bar.THIS);
	}

	@Test
	void lookupBarFailure() {
		assertThat(Bar.fromId(new UUID(0, 10))).isEmpty();
	}

	@RequiredArgsConstructor
	@Accessors(fluent = true)
	@Getter
	private enum Foo implements Identified<Foo, Integer>, Fluent.Identified<Integer> {

		THIS(10),
		THAT(11);

		private final Integer id;

		public static Optional<Foo> fromId(Integer id) {
			return Identified.fromId(Foo.values(), id);
		}

		public static Optional<Foo> fromOffset(int id) {
			return Identified.fromOffset(Foo.values(), id, 10);
		}

	}

	@RequiredArgsConstructor
	@Getter
	private enum Bar implements Identified<Bar, UUID> {

		THIS(new UUID(0, 1)),
		THAT(new UUID(0, 2));

		private final UUID id;

		public static Optional<Bar> fromId(UUID id) {
			return Identified.fromId(Bar.values(), id);
		}

	}

}