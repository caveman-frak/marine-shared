package uk.co.bluegecko.marine.shared.mapper;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import javax.measure.Quantity;
import javax.measure.Unit;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson object mappers configuration, including support for Unit of Measure.
 *
 * @see <a href="https://unitsofmeasurement.github.io/">Unit Of Measure</a> classes.
 */
@Configuration
public class JacksonConfiguration {

	/**
	 * Add object mapper support for Unit Of Measure.
	 *
	 * @return builder with {@link JsonSerializer}s and {@link JsonDeserializer}s for {@link Unit} and {@link Quantity}.
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer registerUnitOfMeasureModule() {
		return builder -> builder.modules(new MarineJacksonModule());
	}

}