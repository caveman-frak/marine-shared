package uk.co.bluegecko.marine.shared.configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.random.RandomGenerator;
import lombok.NonNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import uk.co.bluegecko.marine.shared.clock.SteppingClock;
import uk.co.bluegecko.marine.shared.random.SteppingGenerator;

@TestConfiguration
public class TestApplicationConfiguration extends SharedConfiguration implements DateTimeFixture {

	@SuppressWarnings("SameReturnValue")
	@Bean
	public ZoneId zone() {
		return ZONE;
	}

	@Bean
	public LocalDate date() {
		return LocalDate.of(YEAR, MONTH, DAY);
	}

	@Bean
	public LocalTime time() {
		return LocalTime.of(HOUR, MINUTE, SECOND);
	}

	@Bean
	public LocalDateTime dateTime(@NonNull LocalDate date, @NonNull LocalTime time) {
		return LocalDateTime.of(date, time);
	}

	@Bean
	public ZonedDateTime zonedDateTime(@NonNull LocalDateTime dateTime, @NonNull ZoneId zone) {
		return ZonedDateTime.of(dateTime, zone);
	}

	@Bean
	public ZoneOffset zoneOffset(LocalDateTime dateTime, ZoneId zone) {
		return zone.getRules().getOffset(dateTime);
	}

	@Bean
	public OffsetDateTime offsetDateTime(@NonNull LocalDateTime dateTime, @NonNull ZoneId zone) {
		return OffsetDateTime.of(dateTime, zone.getRules().getOffset(dateTime));
	}

	@Bean
	public Instant instant(@NonNull ZonedDateTime dateTime) {
		return dateTime.toInstant();
	}

	@Bean
	@Primary
	public SteppingClock clock(@NonNull Instant instant, @NonNull ZoneId zone) {
		return SteppingClock.stepping(instant, zone);
	}

	@Override
	@Bean
	@Primary
	public RandomGenerator randomGenerator() {
		return new SteppingGenerator();
	}

}