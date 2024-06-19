package uk.co.bluegecko.marine.shared.configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSharedConfiguration {

	/**
	 * This is the same value as the more verbose 15-Jun-2000 12:30:10 UTC used in the test project.
	 */
	public static final int EPOCH_SECOND = 961072210;

	@Bean
	public Clock clock() {
		return Clock.fixed(Instant.ofEpochSecond(EPOCH_SECOND), ZoneOffset.UTC);
	}

}