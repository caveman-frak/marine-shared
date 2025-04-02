package uk.co.bluegecko.marine.shared.utility;

import static uk.co.bluegecko.marine.shared.utility.Fmt.fmt;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

@Slf4j
public final class Debug {

	private Debug() {
		throw new UnsupportedOperationException();
	}

	public static void debug(String message, Object... args) {
		custom(() -> System.out, Level.DEBUG, message, args);
	}

	public static void error(String message, Object... args) {
		custom(() -> System.err, Level.ERROR, message, args);
	}

	public static void custom(Supplier<OutputStream> output, Level level, String message, Object... args) {
		try (PrintWriter writer = Optional.ofNullable(System.console()).map(Console::writer)
				.orElseGet(() -> new PrintWriter(output.get()))) {
			writer.printf(message + '\n', fmt(args));
		}
		if (log.isEnabledForLevel(level)) {
			log.atLevel(level).log(message, fmt(args));
		}
	}

	public static void env(Environment environment) {
		env(environment, k -> true, System.out);
	}

	public static void env(Environment environment, Predicate<String> filter, PrintStream out) {
		if (environment instanceof AbstractEnvironment env) {
			out.println(">>>>>>>>>> Dumping environment >>>>>>>>>>");
			env.getPropertySources().stream()
					.filter(EnumerablePropertySource.class::isInstance)
					.map(EnumerablePropertySource.class::cast)
					.flatMap(ps -> Arrays.stream(ps.getPropertyNames()))
					.filter(filter)
					.distinct()
					.sorted()
					.forEachOrdered(k -> out.printf("%s: %s\n", k, env.getProperty(k)));
			out.println("<<<<<<<<<<");
		} else {
			out.println("========== Unable to dump environment! ==========");
		}
	}

}