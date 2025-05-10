package uk.co.bluegecko.marine.shared.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.discarding;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.justClass;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.keeping;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.none;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.shortenExcept;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.shortening;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.simple;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.standard;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.truncateExcept;
import static uk.co.bluegecko.marine.shared.utility.Abbreviator.truncating;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbbreviatorTest {

	@ParameterizedTest
	@MethodSource("valueProvider")
	void checkFormat(String description, Abbreviator abbreviator, String result) {
		assertThat(abbreviator.abbreviate(AbbreviatorTest.class)).describedAs(description).isEqualTo(result);
	}

	static Stream<Arguments> valueProvider() {
		return Stream.of(arguments("none", none(), "uk.co.bluegecko.marine.shared.utility.AbbreviatorTest"),
				arguments("class", justClass(), "AbbreviatorTest"),
				arguments("simple", simple(), "u.c.b.m.s.u.AbbreviatorTest"),
				arguments("discard 4", discarding(4), "....shared.utility.AbbreviatorTest"),
				arguments("retain 4", keeping(4), "..bluegecko.marine.shared.utility.AbbreviatorTest"),
				arguments("truncate 4", truncating(4), "u.c.b.m.shared.utility.AbbreviatorTest"),
				arguments("truncate before 4", truncateExcept(4),
						"u.c.bluegecko.marine.shared.utility.AbbreviatorTest"),
				arguments("shorten 4", shortening(4, 3), "uk.co.blu.mar.shared.utility.AbbreviatorTest"),
				arguments("shortening before 2", shortenExcept(3, 3),
						"uk.co.blu.marine.shared.utility.AbbreviatorTest"),
				arguments("standard", standard(), "u.c.b.m.shared.utilit.AbbreviatorTest"));
	}

}