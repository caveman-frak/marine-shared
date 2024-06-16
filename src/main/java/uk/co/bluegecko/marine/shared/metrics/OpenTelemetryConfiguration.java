package uk.co.bluegecko.marine.shared.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfiguration {

	@Bean
	@ConditionalOnClass(name = "io.opentelemetry.javaagent.OpenTelemetryAgent")
	public MeterRegistry otelRegistry() {
		Optional<MeterRegistry> otelRegistry = Metrics.globalRegistry.getRegistries().stream()
				.filter(r -> r.getClass().getName().contains("OpenTelemetryMeterRegistry"))
				.findAny();
		otelRegistry.ifPresent(Metrics.globalRegistry::remove);
		return otelRegistry.orElse(null);
	}

}