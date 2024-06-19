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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.marine.shared.configuration.TestSharedConfiguration;

@SpringJUnitConfig
@Import(LoggingMetricsConfiguration.class)
class LoggingMetricsConfigurationTest {

	@MockBean
	Config config;
	@MockBean
	LoggingMeterRegistry registry;

	List<MeterFilter> filters;

	@BeforeEach
	void setUpConfig() {
		filters = new ArrayList<>();
		when(registry.config()).thenReturn(config);
		when(config.meterFilter(any())).thenAnswer(i -> {
			filters.add(i.getArgument(0, MeterFilter.class));
			return config;
		});
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

		assertThat(filterAndReduceReply(metric(name))).isEqualTo(reply);
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

		assertThat(filterAndReduceReply(metric(name))).isEqualTo(reply);
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

		assertThat(filterAndReduceReply(metric(name))).isEqualTo(reply);
	}

	@ParameterizedTest
	@CsvSource({
			"marine, deny", "marine.bar, deny", "marine.foo, deny", "foo, neutral", "bar, deny"
	})
	void filterDifferent(String name, @ConvertWith(ReplyFromStringConverter.class) MeterFilterReply reply) {
		MeterRegistryCustomizer<LoggingMeterRegistry> customizer = loggingMeterCustomizer(
				new String[]{}, new String[]{"foo"});

		customizer.customize(registry);
		assertThat(filters).hasSize(1);

		assertThat(filterAndReduceReply(metric(name))).isEqualTo(reply);
	}

	@Nested
	class BeanCreationWithDefault {

		@Autowired
		ApplicationContext context;

		@Test
		void registryConfigBean() {
			LoggingRegistryConfig registryConfig = context.getBean(LoggingRegistryConfig.class);
			assertThat(registryConfig).isNotNull();
			assertThat(registryConfig.get("logging.enabled")).isEqualTo("true");
			assertThat(registryConfig.get("logging.step")).isEqualTo("1m");
			assertThat(registryConfig.get("logging.logInactive")).isEqualTo("false");
			assertThat(registryConfig.enabled()).isEqualTo(true);
			assertThat(registryConfig.step()).isEqualTo(Duration.ofSeconds(60));
			assertThat(registryConfig.logInactive()).isEqualTo(false);
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
			assertThat(filters).hasSize(1);
		}

		@Test
		void loggingMeterRegistryBean() {
			LoggingMeterRegistry meterRegistry = context.getBean(LoggingMeterRegistry.class);
			assertThat(meterRegistry).isNotNull();
		}

	}

	@Nested
	@TestPropertySource(properties = {
			"marine.metrics.logging.enabled=false",
			"marine.metrics.logging.step=10s",
			"marine.metrics.logging.logInactive=true",
			"marine.metrics.logging.filter.allow=marine.foo,marine.bar",
			"marine.metrics.logging.filter.deny=foo,bar"
	})
	class BeanCreationWithProperties {

		@Autowired
		ApplicationContext context;

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
			assertThat(filters).hasSize(4);
		}

	}

	private BinaryOperator<MeterFilterReply> accumulator() {
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

	private static Id metric(String name) {
		return new Id(name, Tags.of("foo", "bar"), null, null, Type.COUNTER);
	}

	private MeterFilterReply filterAndReduceReply(Id metric) {
		return filters.stream().map(f -> f.accept(metric)).reduce(MeterFilterReply.NEUTRAL, accumulator());
	}

	@Configuration
	static class TestConfiguration extends TestSharedConfiguration {

	}

}