package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.marine.shared.utility.function.Safe.equal;
import static uk.co.bluegecko.marine.shared.utility.function.Safe.safe;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

class SafeTest {

	@Test
	void checkSafeEqual() {
		assertThat(equal("Foo").test("Foo")).describedAs("Foo==Foo").isTrue();
		assertThat(equal("Foo").test("Bar")).describedAs("Foo==Bar").isFalse();
		assertThat(equal("Bar").test("Foo")).describedAs("Bar==Foo").isFalse();
		assertThat(equal("Foo").test(null)).describedAs("Foo==null").isFalse();
		assertThat(equal(null).test("Foo")).describedAs("null==Foo").isFalse();
		assertThat(equal(null).test(null)).describedAs("null==null").isTrue();
	}

	@Test
	void testSafePredicate() {
		Predicate<String> blankOrEmpty = a -> a.trim().isEmpty();

		assertThat(safe(blankOrEmpty).test("Foo")).describedAs("Foo").isFalse();
		assertThat(safe(blankOrEmpty).test("")).describedAs("empty").isTrue();
		assertThat(safe(blankOrEmpty).test("   ")).describedAs("blank").isTrue();
		assertThat(safe(blankOrEmpty).test(null)).describedAs("null").isFalse();
	}

	@Test
	void testSafeFunction() {
		Function<String, String> lowercase = a -> a.trim().toLowerCase();

		assertThat(safe(lowercase).apply("Foo")).describedAs("Foo").isEqualTo("foo");
		assertThat(safe(lowercase).apply("   ")).describedAs("blank").isEqualTo("");
		assertThat(safe(lowercase).apply(null)).describedAs("null").isNull();
	}

	@Test
	void testSafeUnaryOperator() {
		UnaryOperator<String> lowercase = a -> a.trim().toLowerCase();

		assertThat(safe(lowercase).apply("Foo")).describedAs("Foo").isEqualTo("foo");
		assertThat(safe(lowercase).apply("")).describedAs("empty").isEqualTo("");
		assertThat(safe(lowercase).apply("   ")).describedAs("blank").isEqualTo("");
		assertThat(safe(lowercase).apply(null)).describedAs("null").isNull();
	}

	@Test
	void testSafeBiPredicate() {
		BiPredicate<String, Integer> blankOrEmpty = (a, b) -> a.trim().isEmpty();

		assertThat(safe(blankOrEmpty).test("Foo", 1)).describedAs("Foo/1").isFalse();
		assertThat(safe(blankOrEmpty).test("", 1)).describedAs("empty/1").isTrue();
		assertThat(safe(blankOrEmpty).test("   ", 1)).describedAs("blank/1").isTrue();
		assertThat(safe(blankOrEmpty).test(null, 1)).describedAs("null/1").isFalse();
		assertThat(safe(blankOrEmpty).test("", null)).describedAs("empty/null").isFalse();
		assertThat(safe(blankOrEmpty).test(null, null)).describedAs("null/null").isFalse();
	}

	@Test
	void testSafeBiFunction() {
		BiFunction<String, Boolean, String> lowerOrUpperCase =
				(a, b) -> b ? a.trim().toLowerCase() : a.trim().toUpperCase();

		assertThat(safe(lowerOrUpperCase).apply("Foo", true)).describedAs("Foo/true").isEqualTo("foo");
		assertThat(safe(lowerOrUpperCase).apply("Foo", false)).describedAs("Foo/false").isEqualTo("FOO");
		assertThat(safe(lowerOrUpperCase).apply("   ", true)).describedAs("blank/true").isEqualTo("");
		assertThat(safe(lowerOrUpperCase).apply(null, true)).describedAs("null/true").isNull();
		assertThat(safe(lowerOrUpperCase).apply("Foo", null)).describedAs("Foo/null").isNull();
		assertThat(safe(lowerOrUpperCase).apply(null, null)).describedAs("null/null").isNull();
	}

	@Test
	void testSafeBiOperator() {
		BinaryOperator<String> lowerOrUpperCase =
				(a, b) -> Boolean.parseBoolean(b) ? a.trim().toLowerCase() : a.trim().toUpperCase();

		assertThat(safe(lowerOrUpperCase).apply("Foo", "true")).describedAs("Foo/true").isEqualTo("foo");
		assertThat(safe(lowerOrUpperCase).apply("Foo", "false")).describedAs("Foo/false").isEqualTo("FOO");
		assertThat(safe(lowerOrUpperCase).apply("   ", "true")).describedAs("blank/true").isEqualTo("");
		assertThat(safe(lowerOrUpperCase).apply(null, "true")).describedAs("null/true").isNull();
		assertThat(safe(lowerOrUpperCase).apply("Foo", null)).describedAs("Foo/null").isNull();
		assertThat(safe(lowerOrUpperCase).apply(null, null)).describedAs("null/null").isNull();
	}


}