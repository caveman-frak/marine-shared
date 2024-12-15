package uk.co.bluegecko.marine.shared.configuration;

import java.time.Month;
import java.time.ZoneOffset;

public interface DateTimeFixture {

	// Default test date/time, as an ISO string.
	String ISO_STR = "2000-06-15T12:30:10.000Z";
	// Default test date/time constants
	ZoneOffset ZONE = ZoneOffset.UTC;
	int YEAR = 2000;
	Month MONTH = Month.JUNE;
	int DAY = 15;
	int HOUR = 12;
	int MINUTE = 30;
	int SECOND = 10;

	// expressed as seconds and milliseconds since start of epoch
	long EPOCH_SECOND = 1592224210;
	long EPOCH_MILLI = EPOCH_SECOND * 1000;

}