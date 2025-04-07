package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.marine.shared.utility.function.NullOr.nullIsEqual;
import static uk.co.bluegecko.marine.shared.utility.function.NullOr.nullIsTrue;
import static uk.co.bluegecko.marine.shared.utility.function.NullOr.nullOrEquals;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class NullOrTest {

	@Test
	void testNullIsTrue() {
		Predicate<String> blankOrEmpty = a -> a.trim().isEmpty();

		assertThat(nullIsTrue(blankOrEmpty).test("Foo")).describedAs("Foo").isFalse();
		assertThat(nullIsTrue(blankOrEmpty).test("")).describedAs("empty").isTrue();
		assertThat(nullIsTrue(blankOrEmpty).test("   ")).describedAs("blank").isTrue();
		assertThat(nullIsTrue(blankOrEmpty).test(null)).describedAs("null").isTrue();
	}

	@Test
	void testNullIsEqual() {
		assertThat(nullIsEqual("Foo").test("Foo")).describedAs("Foo==Foo").isTrue();
		assertThat(nullIsEqual("Foo").test("Bar")).describedAs("Foo==Bar").isFalse();
		assertThat(nullIsEqual("Bar").test("Foo")).describedAs("Bar==Foo").isFalse();
		assertThat(nullIsEqual("Foo").test(null)).describedAs("Foo==null").isTrue();
		assertThat(nullIsEqual(null).test("Foo")).describedAs("null==Foo").isTrue();
		assertThat(nullIsEqual(null).test(null)).describedAs("null==null").isTrue();
	}

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
		assertThat(NullOr.<String, String>nullOrEquals(String::toLowerCase, null).test(null))
				.isTrue();
	}

	@Test
	void testNotNullWithExtractIsEqual() {
		assertThat(NullOr.<String, String>nullOrEquals(String::toLowerCase, "foo").test("Foo"))
				.isTrue();
	}

	@Test
	void testNotNullWithExtractIsNotEqual() {
		assertThat(NullOr.<String, String>nullOrEquals(String::toLowerCase, "foo").test("Bar"))
				.isFalse();
	}

}