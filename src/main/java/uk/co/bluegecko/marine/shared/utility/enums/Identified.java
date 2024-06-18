package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Identified<T extends Enum<T>> extends uk.co.bluegecko.marine.shared.utility.Identified<Integer> {

	static <T extends Identified<?>> Optional<T> fromId(T[] values, int index, int offset) {
		int pos = index - offset;
		return pos >= 0 && pos < values.length ? Optional.of(values[pos]) : Optional.empty();
	}

	static <T extends Identified<?>> Optional<T> fromId(T[] values, int id) {
		return Arrays.stream(values).filter(e -> e.getId().equals(id)).findFirst();
	}

}