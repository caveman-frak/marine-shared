package uk.co.bluegecko.marine.shared.utility;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.env.ConfigurableEnvironment;
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
			writer.printf(message + '\n', Fmt.format(args));
		}
		if (log.isEnabledForLevel(level)) {
			log.atLevel(level).log(message, Fmt.format(args));
		}
	}

	public static void env(Environment environment) {
		env(environment, s -> true, System.out);
	}

	public static void env(Environment environment, Predicate<String> filter, PrintStream out) {
		out.print(env(environment, filter));
	}

	public static void env(Environment environment, Predicate<String> filter, Level level) {
		log.atLevel(level).log(env(environment, filter).toString());
	}

	public static StringBuilder env(Environment environment, Predicate<String> filter) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(" Active profiles: ")
				.append(Arrays.stream(environment.getActiveProfiles()).distinct()
						.collect(Collectors.joining(",")))
				.append('\r')
				.append("Default profiles: ")
				.append(Arrays.stream(environment.getDefaultProfiles()).distinct()
						.collect(Collectors.joining(",")))
				.append('\r');
		if (environment instanceof ConfigurableEnvironment env) {
			buffer.append(">>>>>>>>>> Dumping Environment >>>>>>>>>>\r");
			env.getPropertySources().stream()
					.filter(EnumerablePropertySource.class::isInstance)
					.map(EnumerablePropertySource.class::cast)
					.flatMap(ps -> Arrays.stream(ps.getPropertyNames()))
					.filter(filter)
					.distinct()
					.sorted()
					.forEachOrdered(k -> buffer.append(String.format("\t%s: %s%n", k, env.getProperty(k))));
			buffer.append("<<<<<<<<<<<< End of Environment Vars >>>>>>>>>>>>\r");
		} else {
			buffer.append("========== Unable to dump environment! ==========\r");
		}
		return buffer;
	}

}