package uk.co.bluegecko.marine.shared.data.converter;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.AttributeConverter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListToStringConverterTest {

	private AttributeConverter<List<String>, String> converter;

	@BeforeEach
	void setUp() {
		converter = new ListToStringConverter();
	}

	@Test
	void convertToDb() {
		assertThat(converter.convertToDatabaseColumn(List.of("One", "Two"))).isEqualTo("One;Two");
	}

	@Test
	void convertToDbWithEmpty() {
		assertThat(converter.convertToDatabaseColumn(List.of())).isEqualTo("");
	}

	@Test
	void convertToDbWithNull() {
		assertThat(converter.convertToDatabaseColumn(null)).isEqualTo("");
	}

	@Test
	void convertFromDb() {
		assertThat(converter.convertToEntityAttribute("One;Two"))
				.hasSize(2).containsExactly("One", "Two");
	}

	@Test
	void convertFromDbWithEmpty() {
		assertThat(converter.convertToEntityAttribute("")).isNotNull().isEmpty();
	}

	@Test
	void convertFromDbWithNull() {
		assertThat(converter.convertToEntityAttribute(null)).isNotNull().isEmpty();
	}
}