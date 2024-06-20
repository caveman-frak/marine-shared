package uk.co.bluegecko.marine.shared.data.converter;

import static org.springframework.util.StringUtils.hasText;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

	private static final String SPLIT_CHAR = ";";

	@Override
	public String convertToDatabaseColumn(List<String> stringList) {
		return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
	}

	@Override
	public List<String> convertToEntityAttribute(String string) {
		return hasText(string) ? Arrays.asList(string.split(SPLIT_CHAR)) : List.of();
	}

}