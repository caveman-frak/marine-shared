package uk.co.bluegecko.marine.shared.test.base;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uk.co.bluegecko.marine.shared.clock.SteppingClock;
import uk.co.bluegecko.marine.shared.configuration.DateTimeFixture;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter(AccessLevel.PROTECTED)
@Accessors(fluent = true, makeFinal = true)
public abstract class DatedTest extends BaseTest implements DateTimeFixture {

	LocalDate date;
	LocalTime time;
	ZoneOffset zone;
	SteppingClock clock;

	protected DatedTest() {
		date = LocalDate.of(YEAR, MONTH, DAY);
		time = LocalTime.of(HOUR, MINUTE, SECOND);
		zone = ZONE;
		clock = SteppingClock.stepping(instant(), zone);
	}

	protected Instant instant() {
		return LocalDateTime.of(date, time).toInstant(zone);
	}

}