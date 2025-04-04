package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Codified<T extends Enum<T>> extends uk.co.bluegecko.marine.shared.utility.Codified {

	static <T extends Enum<T> & Codified<T>> Optional<T> fromCode(T[] values, String code) {
		return uk.co.bluegecko.marine.shared.utility.Codified.fromCode(Arrays.stream(values), code);
	}

}