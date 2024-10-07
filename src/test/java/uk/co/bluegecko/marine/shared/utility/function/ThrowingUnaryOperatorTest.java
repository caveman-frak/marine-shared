package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

class ThrowingUnaryOperatorTest {

	private ThrowingUnaryOperator<String, IOException> c;

	@Test
	void withIoException() {
		c = (a) -> {
			throw new IOException(a);
		};
	}

	@Test
	void withNotIoException() {
		c = (a) -> {
			// compiler error if trying to use wrong exception type, as wanted
//			throw new ParseException(a, b);
			return a;
		};
	}

	@Test
	void withRuntimeException() {
		c = (a) -> {
			throw new RuntimeException(new ParseException(a, 0));
		};
	}

	@Test
	void doQuietly() {
		c = (a) -> {
			throw new IOException(a);
		};
		assertThatThrownBy(() -> QuietFunctions.quietOperator(c).apply("foo"))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasCauseInstanceOf(IOException.class)
				.hasRootCauseMessage("foo");
	}

	@Test
	void doQuietlyWithoutException() {
		c = (a) -> {
			if (a.endsWith("bar")) {
				throw new IOException(a);
			} else {
				return a;
			}
		};
		QuietFunctions.quietOperator(c).apply("foo");
	}

	@Test
	void doQuietlyWithRuntime() {
		c = (a) -> {
			throw new RuntimeException(a);
		};
		assertThatThrownBy(() -> QuietFunctions.quietOperator(c).apply("foo"))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasNoCause();
	}

}