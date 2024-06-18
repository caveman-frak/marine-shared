package uk.co.bluegecko.marine.shared.configuration;

import static org.apache.commons.text.WordUtils.capitalizeFully;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.time.Clock;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
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

	/**
	 * Build a message source for the passed in bundles.
	 *
	 * @param bundleNames the bundles to use.
	 * @return the message source
	 */
	public MessageSource messageSource(String... bundleNames) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(bundleNames);
		return messageSource;
	}

	/**
	 * Create an {@link OpenAPI} bean with application information.
	 *
	 * @param name        the application name property or default.
	 * @param description the application description property or default.
	 * @param version     the application version property or default.
	 * @param licence     the product licence property or default (MIT).
	 * @return the OpenApi bean.
	 */
	@Bean
	public OpenAPI customOpenAPI(
			@Value("${spring.application.name:Unknown Application}") String name,
			@Value("${app.description:Unknown Description}") String description,
			@Value("${app.version:Unknown Version}") String version,
			@Value("${app.licence:http://localhost:8080/licence.txt}") String licence) {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title(capitalizeFully(name)).description(description).version(version)
						.license(new License().name("M.I.T.").url(licence)));
	}

}