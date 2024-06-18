package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Codified<T extends Enum<T>> extends uk.co.bluegecko.marine.shared.utility.Codified {

	static <T extends Codified<?>> Optional<T> fromCode(T[] values, String code) {
		return Arrays.stream(values).filter(e -> e.getCode().equals(code)).findFirst();
	}

}