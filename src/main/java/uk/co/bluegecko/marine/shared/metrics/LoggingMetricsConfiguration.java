package uk.co.bluegecko.marine.shared.metrics;

import static java.lang.String.join;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.time.temporal.ChronoField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class LoggingMetricsConfiguration {

	private static final String LOGGING = "marine.metrics.";

	/**
	 * Configuration bean for logging registry.
	 *
	 * @param environment the environment to use for looking up property values.
	 * @return logging registry configuration.
	 */
	@Bean
	public LoggingRegistryConfig loggingRegistryConfig(Environment environment) {
		return key -> switch (key) {
			case "logging.enabled" -> environment.getProperty(LOGGING + key, "true");
			case "logging.step" -> environment.getProperty(LOGGING + key, "1m");
			case "logging.logInactive" -> environment.getProperty(LOGGING + key, "false");
			default -> null;
		};
	}

	/**
	 * Create a logging registry to periodically log metrics.
	 *
	 * @param config the logging config used to specify step, etc.
	 * @return the logging registry.
	 */
	@Bean
	public LoggingMeterRegistry loggingMeterRegistry(LoggingRegistryConfig config, Clock clock) {
		return LoggingMeterRegistry.builder(config).clock(clock).build();
	}

	/**
	 * Create customizer for {@link LoggingMeterRegistry} to only accept Geovs metrics.
	 *
	 * @param allow the allow to filter on.
	 * @return the customizer.
	 */
	@Bean
	public static MeterRegistryCustomizer<LoggingMeterRegistry> loggingMeterCustomizer(
			@Value("${marine.metrics.logging.filter.deny:}") String[] deny,
			@Value("${marine.metrics.logging.filter.allow:marine}") String[] allow) {
		log.info("Logging metrics that start with `{}` and excluding '{}'", join(", ", allow), join(", ", deny));
		return registry -> {
			for (String denied : deny) {
				registry.config().meterFilter(MeterFilter.deny(meter -> meter.getName().startsWith(denied)));
			}
			for (String allowed : allow) {
				registry.config().meterFilter(MeterFilter.denyUnless(meter -> meter.getName().startsWith(allowed)));
			}
		};
	}

	@Bean
	public Clock micrometerClock(java.time.Clock clock) {
		return new MicrometerClock(clock);
	}

	public record MicrometerClock(java.time.Clock clock) implements Clock {

		@Override
		public long wallTime() {
			return clock.millis();
		}

		@Override
		public long monotonicTime() {
			return clock.instant().getLong(ChronoField.NANO_OF_DAY);
		}
	}

}