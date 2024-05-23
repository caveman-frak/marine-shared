package uk.co.bluegecko.marine.shared.metrics;

import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class LoggingMetricsConfiguration {

	private static final String LOGGING = "management.metrics.export.";

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
	public LoggingMeterRegistry loggingMeterRegistry(LoggingRegistryConfig config) {
		return LoggingMeterRegistry.builder(config).build();
	}

	/**
	 * Create customizer for {@link LoggingMeterRegistry} to only accept Geovs metrics.
	 *
	 * @param prefix the prefix to filter on.
	 * @return the customizer.
	 */
	@Bean
	public MeterRegistryCustomizer<LoggingMeterRegistry> loggingMeterCustomizer(
			@Value("${marine.metrics.logging.filter.prefix:marine}") String prefix) {
		log.info("Logging only metrics that start with `{}`", prefix);
		return registry -> registry.config()
				.meterFilter(MeterFilter.denyUnless(meter -> meter.getName().startsWith(prefix)));
	}

}