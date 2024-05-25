package uk.co.bluegecko.marine.shared.utility;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@UtilityClass
@Slf4j
public class Debug {

	public void debug(String message, Object... args) {
		printf(() -> System.out, Level.DEBUG, message, args);
	}

	public void error(String message, Object... args) {
		printf(() -> System.err, Level.ERROR, message, args);
	}

	private void printf(Supplier<OutputStream> out, Level level, String message, Object... args) {
		try (PrintWriter writer = Optional.ofNullable(System.console()).map(Console::writer)
				.orElseGet(() -> new PrintWriter(out.get()))) {
			writer.printf(message + "\n", args);
		}
		if (log.isEnabledForLevel(level)) {
			log.atLevel(level).log(message, args);
		}
	}

}