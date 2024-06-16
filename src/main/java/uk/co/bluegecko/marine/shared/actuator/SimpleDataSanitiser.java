package uk.co.bluegecko.marine.shared.actuator;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.SanitizableData;
import org.springframework.boot.actuate.endpoint.SanitizingFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sanitize properties through the actuator endpoints.
 */
@Configuration
public class SimpleDataSanitiser {

	private static final Stream<String> DEFAULT_KEYS = Stream.of(
			"password", "secret", "key", "token", "private", "user.name");

	/**
	 * Property keys that should be sanitized. Merged set of default keys and additional keys.
	 *
	 * @param additionalKeys additional keys that should also be sanitized.
	 * @return predicates for the keys (regex keys must be matched, others just contained).
	 */
	@Bean
	Set<Predicate<String>> keysToSanitise(@Value("${marine.sanitise.keys:}") Set<String> additionalKeys) {
		return Stream.concat(DEFAULT_KEYS, additionalKeys.stream())
				.map(SimpleDataSanitiser::toPredicate).collect(Collectors.toSet());
	}

	/**
	 * Sanitize a single key if required, only uses key name to check.
	 *
	 * @param keys the set of keys that require sanitization.
	 * @return the original value unless sanitized.
	 */
	@Bean
	SanitizingFunction dataSanitiser(Set<Predicate<String>> keys) {
		return d -> keys.stream().anyMatch(p -> p.test(d.getKey())) ?
				d.withValue(SanitizableData.SANITIZED_VALUE) : d;
	}

	private static Predicate<String> toPredicate(String k) {
		return isRegex(k) ? Pattern.compile(k).asMatchPredicate() : Pattern.compile(k).asPredicate();
	}

	private static boolean isRegex(String k) {
		return k.contains("*") || k.contains("$") || k.contains("^") || k.contains("?");
	}

}