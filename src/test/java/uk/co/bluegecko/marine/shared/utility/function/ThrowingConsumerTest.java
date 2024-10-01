package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.co.bluegecko.marine.shared.utility.function.ThrowingConsumer.quietConsumer;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

class ThrowingConsumerTest {

	private ThrowingConsumer<String, IOException> c;

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
		};
	}

	@Test
	void withRuntimeException() {
		c = (a) -> {
			throw new RuntimeException(new ParseException(a, 0));
		};
	}

	@Test
	void doQuietConsumer() {
		c = (a) -> {
			throw new IOException(a);
		};
		assertThatThrownBy(() -> quietConsumer(c).accept("foo"))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasCauseInstanceOf(IOException.class)
				.hasRootCauseMessage("foo");
	}

	@Test
	void doQuietConsumerWithoutException() {
		c = (a) -> {
			if (a.endsWith("bar")) {
				throw new IOException(a);
			}
		};
		quietConsumer(c).accept("foo");
	}

	@Test
	void doQuietConsumerWithRuntime() {
		c = (a) -> {
			throw new RuntimeException(a);
		};
		assertThatThrownBy(() -> quietConsumer(c).accept("foo"))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasNoCause();
	}

}