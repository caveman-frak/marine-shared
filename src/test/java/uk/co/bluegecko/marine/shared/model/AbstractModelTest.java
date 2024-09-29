package uk.co.bluegecko.marine.shared.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import uk.co.bluegecko.marine.shared.clock.SteppingClock;

public abstract class AbstractModelTest {

	protected SteppingClock clock;

	protected void setUpClock() {
		clock = SteppingClock.stepping(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(12, 0));
	}

}