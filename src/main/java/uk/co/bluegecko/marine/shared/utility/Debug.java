package uk.co.bluegecko.marine.shared.utility;

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

@UtilityClass
@Slf4j
public class Debug {

	public void debug(String message, Object... args) {
		custom(() -> System.out, Level.DEBUG, message, args);
	}

	public void error(String message, Object... args) {
		custom(() -> System.err, Level.ERROR, message, args);
	}

	public void custom(Supplier<OutputStream> out, Level level, String message, Object... args) {
		try (PrintWriter writer = Optional.ofNullable(System.console()).map(Console::writer)
				.orElseGet(() -> new PrintWriter(out.get()))) {
			writer.printf(message + "\n", args);
		}
		if (log.isEnabledForLevel(level)) {
			log.atLevel(level).log(message, args);
		}
	}

	public void dump(Environment env) {
		dump(env, k -> true, System.out);
	}

	public void dump(Environment env, Predicate<String> filter, PrintStream out) {
		if (env instanceof AbstractEnvironment environment) {
			System.out.println(">>>>>>>>>> Dumping environment:");
			environment.getPropertySources().stream()
					.filter(EnumerablePropertySource.class::isInstance)
					.map(EnumerablePropertySource.class::cast)
					.flatMap(ps -> Arrays.stream(ps.getPropertyNames()))
					.filter(filter)
					.distinct()
					.sorted()
					.forEachOrdered(k -> out.printf("%s: %s\n", k, environment.getProperty(k)));
			out.println("<<<<<<<<<<");
		} else {
			System.out.println("========== Unable to dump environment!");
		}
	}

}