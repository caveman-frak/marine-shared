package uk.co.bluegecko.marine.shared.utility.function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.co.bluegecko.marine.shared.utility.function.QuietFunctions.quietSupplier;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

class ThrowingSupplierTest {

	private ThrowingSupplier<String, IOException> c;

	@Test
	void withIoException() {
		c = () -> {
			throw new IOException("foo");
		};
	}

	@Test
	void withNotIoException() {
		c = () -> {
			// compiler error if trying to use wrong exception type, as wanted
//			throw new ParseException(a, b);
			return "foo";
		};
	}

	@Test
	void withRuntimeException() {
		c = () -> {
			throw new RuntimeException(new ParseException("foo", 0));
		};
	}

	@Test
	void doQuietly() {
		c = () -> {
			throw new IOException("foo");
		};
		assertThatThrownBy(() -> quietSupplier(c).get())
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasCauseInstanceOf(IOException.class)
				.hasRootCauseMessage("foo");
	}

	@Test
	void doQuietlyWithoutException() {
		c = () -> {
			if ("foo".endsWith("bar")) {
				throw new IOException("foo");
			} else {
				return "foo";
			}
		};
		quietSupplier(c).get();
	}

	@Test
	void doQuietlyWithRuntime() {
		c = () -> {
			throw new RuntimeException("foo");
		};
		assertThatThrownBy(() -> quietSupplier(c).get())
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo")
				.hasNoCause();
	}

}