package uk.co.bluegecko.marine.shared.configuration;

import static org.apache.commons.text.WordUtils.capitalizeFully;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.co.bluegecko.marine.shared.properties.YamlPropertySourceFactory;

@Configuration
@PropertySource(name = "Swagger", value = "classpath:swagger-meta-data.yaml",
		factory = YamlPropertySourceFactory.class)
@PropertySource(name = "Swagger", value = "classpath:swagger-controller.yaml",
		factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@PropertySource(name = "Swagger", value = "classpath:swagger-model.yaml",
		factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class SwaggerConfiguration {

	/**
	 * Create an {@link OpenAPI} bean with application information.
	 *
	 * @param name        the application name property or default.
	 * @param description the application description property or default.
	 * @param version     the application version property or default.
	 * @return the OpenApi bean.
	 */
	@Bean
	public OpenAPI customOpenAPI(Environment env,
			@Value("${spring.application.name:Unknown Application}") String name,
			@Value("${app.description:Unknown Description}") String description,
			@Value("${app.version:Unknown Version}") String version) {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title(capitalizeFully(name)).description(description).version(version)
						.license(new License().name(env.getProperty("meta-data.license.name"))
								.url(env.getProperty("meta-data.license.url")))
						.termsOfService(env.getProperty("meta-data.terms.url"))
						.contact(new Contact().name(env.getProperty("meta-data.contact.name"))
								.email(env.getProperty("meta-data.contact.email"))
								.url(env.getProperty("meta-data.contact.url")))
						.summary(env.getProperty("meta-data.summary")));
	}

}