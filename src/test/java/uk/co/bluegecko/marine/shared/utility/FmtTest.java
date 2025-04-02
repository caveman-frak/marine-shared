package uk.co.bluegecko.marine.shared.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.DAY;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.EPOCH_MILLI;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.HOUR;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.MINUTE;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.MONTH;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.SECOND;
import static uk.co.bluegecko.marine.shared.configuration.DateTimeFixture.YEAR;
import static uk.co.bluegecko.marine.shared.utility.Fmt.binary;
import static uk.co.bluegecko.marine.shared.utility.Fmt.fmt;
import static uk.co.bluegecko.marine.shared.utility.Fmt.hex;
import static uk.co.bluegecko.marine.shared.utility.Fmt.octal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FmtTest {

	private static final LocalTime TIME = LocalTime.of(HOUR, MINUTE, SECOND);
	private static final LocalDate DATE = LocalDate.of(YEAR, MONTH, DAY);
	private static final LocalDateTime DATE_TIME = LocalDateTime.of(DATE, TIME);

	@ParameterizedTest
	@MethodSource("valueProvider")
	void checkFormat(String description, Object value, String result) {
		assertThat(fmt(value)).describedAs(description).isEqualTo(result);
	}

	static Stream<Arguments> valueProvider() {
		return Stream.of(arguments("Null", null, "NULL"),
				arguments("Blank", "", "''"),
				arguments("Leading Space", "   Foo", "'   Foo'"),
				arguments("Trailing Space", "Foo   ", "'Foo   '"),
				arguments("String", "Foo", "Foo"),
				arguments("Enum", Foo.FOO, "FOO"),
				arguments("Stream<String>", Stream.of("Foo"), "<Foo>"),
				arguments("Stream<Strings>", Stream.of("Foo", "Bar"), "<Foo,Bar>"),
				arguments("Stream<Enum>", Stream.of(Foo.FOO), "<FOO>"),
				arguments("Stream<Enums>", Stream.of(Foo.FOO, Foo.BAR), "<FOO,BAR>"),
				arguments("List<String>", List.of("Foo"), "(Foo)"),
				arguments("List<Strings>", List.of("Foo", "Bar"), "(Foo,Bar)"),
				arguments("List<Enum>", List.of(Foo.FOO), "(FOO)"),
				arguments("List<Enums>", List.of(Foo.FOO, Foo.BAR), "(FOO,BAR)"),
				arguments("String[]", new String[]{"Foo"}, "[Foo]"),
				arguments("Strings>[]", new String[]{"Foo", "Bar"}, "[Foo,Bar]"),
				arguments("Enum[]", new Foo[]{Foo.FOO}, "[FOO]"),
				arguments("Enums[]", new Foo[]{Foo.FOO, Foo.BAR}, "[FOO,BAR]"),
				arguments("Map<Enum, String>", Map.of(Foo.FOO, "Foo"), "(FOO=Foo)"),
				arguments("Map<Enum, String[]>", Map.of(Foo.FOO, new String[]{"Foo"}), "(FOO=[Foo])"),
				arguments("Int 10", 10, "10"),
				arguments("Int 10m", 10_000_000, "10,000,000"),
				arguments("Long 10", 10L, "10"),
				arguments("BigInt 10", BigInteger.TEN, "10"),
				arguments("Float 10.0", 10.0, "10.0"),
				arguments("Float 10", 10f, "10.0"),
				arguments("Float 1e-6", 0.000_001, "0.000001"),
				arguments("Float 1e-9", 0.000_000_001, "0.0"),
				arguments("Float 10m", 10_000_000.000_001, "10,000,000.000001"),
				arguments("Double 10d", 10d, "10.0"),
				arguments("BigDecimal 10", BigDecimal.TEN, "10.0"),
				arguments("Duration 10m", Duration.ofMinutes(10), "PT10M"),
				arguments("Period 5d", Period.ofDays(5), "P5D"),
				arguments("Time", TIME, "12:30:10"),
				arguments("Date", DATE, "2000-06-15"),
				arguments("DateTime", DATE_TIME, "2000-06-15T12:30:10"),
				arguments("Instant", Instant.ofEpochMilli(EPOCH_MILLI), "2000-06-15T12:30:10Z")
		);
	}

	@Test
	void asHex() {
		assertThat(hex(43)).isEqualTo("0x2B");
	}

	@Test
	void asOctal() {
		assertThat(octal(43)).isEqualTo("053");
	}

	@Test
	void asBinary() {
		assertThat(binary(43)).isEqualTo("0b101011");
	}

	private enum Foo {
		FOO, BAR
	}

}