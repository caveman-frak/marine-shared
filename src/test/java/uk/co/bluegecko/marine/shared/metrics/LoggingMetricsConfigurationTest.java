package uk.co.bluegecko.marine.shared.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static uk.co.bluegecko.marine.shared.metrics.LoggingMetricsConfiguration.loggingMeterCustomizer;

import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Meter.Type;
import io.micrometer.core.instrument.MeterRegistry.Config;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.marine.shared.metrics.LoggingMetricsConfigurationTest.Configuration;

@SpringJUnitConfig
@TestPropertySource(properties = {
		"marine.metrics.logging.enabled=false",
		"marine.metrics.logging.step=10s",
		"marine.metrics.logging.logInactive=true",
		"marine.metrics.logging.filter.deny=foo,bar"
})
@Import({LoggingMetricsConfiguration.class, Configuration.class})
class LoggingMetricsConfigurationTest {

	@Autowired
	ApplicationContext context;

	@MockBean
	Config config;
	@MockBean
	LoggingMeterRegistry registry;

	List<MeterFilter> filters;

	@BeforeEach
	void setUpConfig() {
		filters = new ArrayList<>();
		when(config.meterFilter(any())).thenAnswer(i -> {
			filters.add(i.getArgument(0, MeterFilter.class));
			return config;
		});
	}

	@BeforeEach
	void setUpRegistry() {
		when(registry.config()).thenReturn(config);
	}

	@ParameterizedTest
	@CsvSource({
			"marine, neutral", "marine.bar, neutral", "marine.foo, neutral", "foo, deny", "bar, deny"
	})
	void filterDefault(String name, @ConvertWith(ReplyFromStringConverter.class) MeterFilterReply reply) {
		MeterRegistryCustomizer<LoggingMeterRegistry> customizer = loggingMeterCustomizer(
				new String[]{}, new String[]{"marine"});

		customizer.customize(registry);
		assertThat(filters).hasSize(1);

		Id id = new Id(name, Tags.of("foo", "bar"), null, null, Type.COUNTER);
		assertThat(filters.stream().map(f -> f.accept(id)).toList())
				.hasSize(1).contains(reply);
	}

	@ParameterizedTest
	@CsvSource({
			"marine, deny", "marine.bar, neutral", "marine.foo, neutral", "foo, deny", "bar, deny"
	})
	void filterMorePrecise(String name, @ConvertWith(ReplyFromStringConverter.class) MeterFilterReply reply) {
		MeterRegistryCustomizer<LoggingMeterRegistry> customizer = loggingMeterCustomizer(
				new String[]{}, new String[]{"marine."});

		customizer.customize(registry);
		assertThat(filters).hasSize(1);

		Id id = new Id(name, Tags.of("foo", "bar"), null, null, Type.COUNTER);
		assertThat(filters.stream().map(f -> f.accept(id)).toList())
				.hasSize(1).contains(reply);
	}

	@ParameterizedTest
	@CsvSource({
			"marine, neutral", "marine.bar, neutral", "marine.foo, deny", "foo, deny", "bar, deny"
	})
	void filterDefaultWithDeny(String name, @ConvertWith(ReplyFromStringConverter.class) MeterFilterReply reply) {
		MeterRegistryCustomizer<LoggingMeterRegistry> customizer = loggingMeterCustomizer(
				new String[]{"marine.foo"}, new String[]{"marine"});

		customizer.customize(registry);
		assertThat(filters).hasSize(2);

		Id id = new Id(name, Tags.of("foo", "bar"), null, null, Type.COUNTER);
		assertThat(filters.stream().map(f -> f.accept(id)).reduce(MeterFilterReply.NEUTRAL, accumulate(), combine()))
				.isEqualTo(reply);
	}

	@Test
	void registryConfigBean() {
		LoggingRegistryConfig registryConfig = context.getBean(LoggingRegistryConfig.class);
		assertThat(registryConfig).isNotNull();
		assertThat(registryConfig.get("logging.enabled")).isEqualTo("false");
		assertThat(registryConfig.get("logging.step")).isEqualTo("10s");
		assertThat(registryConfig.get("logging.logInactive")).isEqualTo("true");
		assertThat(registryConfig.enabled()).isEqualTo(false);
		assertThat(registryConfig.step()).isEqualTo(Duration.ofSeconds(10));
		assertThat(registryConfig.logInactive()).isEqualTo(true);
	}

	@Test
	@SuppressWarnings("unchecked")
	void registryCustomizerBean() {
		String[] names = context.getBeanNamesForType(
				ResolvableType.forClassWithGenerics(MeterRegistryCustomizer.class, LoggingMeterRegistry.class));
		MeterRegistryCustomizer<LoggingMeterRegistry> customizer = (MeterRegistryCustomizer<LoggingMeterRegistry>) context.getBean(
				names[0]);
		assertThat(customizer).isNotNull();

		customizer.customize(registry);
		assertThat(filters).hasSize(3);
	}

	@Test
	void loggingMeterRegistryBean() {
		LoggingMeterRegistry meterRegistry = context.getBean(LoggingMeterRegistry.class);
		assertThat(meterRegistry).isNotNull();
	}

	private BiFunction<MeterFilterReply, MeterFilterReply, MeterFilterReply> accumulate() {
		return (previous, current) -> {
			if (previous.equals(MeterFilterReply.DENY) || current.equals(MeterFilterReply.DENY)) {
				return MeterFilterReply.DENY;
			} else if (previous.equals(MeterFilterReply.ACCEPT) || current.equals(MeterFilterReply.ACCEPT)) {
				return MeterFilterReply.ACCEPT;
			} else {
				return MeterFilterReply.NEUTRAL;
			}
		};
	}

	private BinaryOperator<MeterFilterReply> combine() {
		return (previous, current) -> accumulate().apply(previous, current);
	}

	@TestConfiguration
	public static class Configuration {

		@Bean
		public Clock clock() {
			return Clock.systemUTC();
		}
	}

}