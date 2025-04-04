package uk.co.bluegecko.marine.shared.utility.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.shared.utility.Fluent;

class CodifiedTest {

	@Test
	void lookupSuccess() {
		assertThat(Foo.fromCode("this")).isPresent().get().isEqualTo(Foo.THIS);
	}

	@Test
	void lookupFailure() {
		assertThat(Foo.fromCode("other")).isEmpty();
	}

	@RequiredArgsConstructor
	@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
	@Accessors(fluent = true)
	@Getter
	private enum Foo implements Codified<Foo>, Fluent.Codified {

		THIS("this"),
		THAT("that");

		String code;

		public static Optional<Foo> fromCode(String code) {
			return Codified.fromCode(Foo.values(), code);
		}

	}

}