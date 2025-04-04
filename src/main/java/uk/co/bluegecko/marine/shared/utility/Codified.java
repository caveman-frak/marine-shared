package uk.co.bluegecko.marine.shared.utility;

import java.util.Optional;
import java.util.stream.Stream;

public interface Codified {

	String getCode();

	static <T extends Codified> Optional<T> fromCode(Stream<T> values, String code) {
		return values.filter(e -> e.getCode().equals(code)).findFirst();
	}

}