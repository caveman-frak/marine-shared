package uk.co.bluegecko.marine.shared.configuration;

import java.time.Clock;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import uk.co.bluegecko.marine.shared.SharedPackage;

/**
 * Configuration beans for general application use.
 */
@ComponentScan(basePackageClasses = SharedPackage.class)
public abstract class SharedConfiguration {

	/**
	 * Standard Clock instance.
	 *
	 * @return default to {@link Clock#systemUTC()} .
	 */
	@Bean
	public Clock clock() {
		return Clock.systemUTC();
	}

	/**
	 * Standard Random instance.
	 *
	 * @return default to {@link Random}.
	 */
	@Bean
	public RandomGenerator randomGenerator() {
		return new Random();
	}

}