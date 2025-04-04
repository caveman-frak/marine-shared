package uk.co.bluegecko.marine.shared.utility;

import java.util.Optional;

public interface Contiguous extends Identified<Integer> {

	static <T extends Contiguous> Optional<T> fromOffset(
			T[] values, int id, int offset) {
		int index = id - offset;
		return index >= 0 && index < values.length ? Optional.of(values[index]) : Optional.empty();
	}

}