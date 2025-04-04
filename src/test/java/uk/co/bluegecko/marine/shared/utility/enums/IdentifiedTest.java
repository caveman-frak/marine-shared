package uk.co.bluegecko.marine.shared.utility.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.shared.utility.Contiguous;
import uk.co.bluegecko.marine.shared.utility.Fluent;

class IdentifiedTest {

	@Test
	void lookupSuccess() {
		assertThat(Foo.fromId(10)).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFailure() {
		assertThat(Foo.fromId(20)).isEmpty();
	}

	@Test
	void lookupWithOffsetSuccess() {
		assertThat(Foo.fromOffset(10)).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupWithOffsetFailure() {
		assertThat(Foo.fromOffset(20)).isEmpty();
	}

	@RequiredArgsConstructor
	@Accessors(fluent = true)
	@Getter
	private enum Foo implements Identified<Foo, Integer>, Fluent.Contiguous {

		THIS(10),
		THAT(11);

		private final Integer id;

		public static Optional<Foo> fromId(Integer id) {
			return Identified.fromId(Foo.values(), id);
		}

		public static Optional<Foo> fromOffset(int id) {
			return Contiguous.fromOffset(Foo.values(), id, 10);
		}

	}

}