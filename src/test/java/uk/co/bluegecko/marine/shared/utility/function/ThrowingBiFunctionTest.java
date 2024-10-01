package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.co.bluegecko.marine.shared.utility.function.QuietFunctions.quietFunction;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

class ThrowingBiFunctionTest {

	private ThrowingBiFunction<String, Integer, String, IOException> c;

	@Test
	void withIoException() {
		c = (a, _) -> {
			throw new IOException(a);
		};
	}

	@Test
	void withNotIoException() {
		c = (a, b) -> {
			// compiler error if trying to use wrong exception type, as wanted
//			throw new ParseException(a, b);
			return a + "+" + b;
		};
	}

	@Test
	void withRuntimeException() {
		c = (a, b) -> {
			throw new RuntimeException(new ParseException(a, b));
		};
	}

	@Test
	void doQuietly() {
		c = (a, b) -> {
			throw new IOException(a + "-" + b);
		};
		assertThatThrownBy(() -> quietFunction(c).apply("foo", 99))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo-99")
				.hasCauseInstanceOf(IOException.class)
				.hasRootCauseMessage("foo-99");
	}

	@Test
	void doQuietlyWithoutException() {
		c = (a, b) -> {
			if (b % 2 == 0) {
				throw new IOException(a + "-" + b);
			} else {
				return a + "+" + b;
			}
		};
		quietFunction(c).apply("foo", 99);
	}

	@Test
	void doQuietlyWithRuntime() {
		c = (a, b) -> {
			throw new RuntimeException(a + "-" + b);
		};
		assertThatThrownBy(() -> quietFunction(c).apply("foo", 99))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo-99")
				.hasNoCause();
	}

}