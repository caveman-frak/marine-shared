package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.marine.shared.utility.function.NullOrEquals.nullOrEquals;

import org.junit.jupiter.api.Test;

class NullOrEqualsTest {

	@Test
	void testNullNoExtract() {
		assertThat(nullOrEquals(null).test("Foo")).isTrue();
	}

	@Test
	void testNotNullNoExtractIsEqual() {
		assertThat(nullOrEquals("Foo").test("Foo")).isTrue();
	}

	@Test
	void testNotNullNoExtractIsNotEqual() {
		assertThat(nullOrEquals("Foo").test("foo")).isFalse();
	}

	@Test
	void testNullWithExtract() {
		assertThat(NullOrEquals.<String, String>nullOrEquals(null, String::toLowerCase).test(null))
				.isTrue();
	}

	@Test
	void testNotNullWithExtractIsEqual() {
		assertThat(NullOrEquals.<String, String>nullOrEquals("foo", String::toLowerCase).test("Foo"))
				.isTrue();
	}

	@Test
	void testNotNullWithExtractIsNotEqual() {
		assertThat(NullOrEquals.<String, String>nullOrEquals("foo", String::toLowerCase).test("Bar"))
				.isFalse();
	}

}