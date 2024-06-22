package uk.co.bluegecko.marine.shared.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StringTrimConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute == null ? null : attribute.trim();
	}

	@Override
	public String convertToEntityAttribute(String database) {
		return database == null ? null : database.trim();
	}

}