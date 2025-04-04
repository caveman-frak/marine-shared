package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Identified<T extends Enum<T>, I> extends
		uk.co.bluegecko.marine.shared.utility.Identified<I> {

	static <T extends Identified<?, Integer>> Optional<T> fromOffset(
			T[] values, int id, int offset) {
		int index = id - offset;
		return index >= 0 && index < values.length ? Optional.of(values[index]) : Optional.empty();
	}

	static <I, T extends Identified<?, I>> Optional<T> fromId(T[] values, I id) {
		return Arrays.stream(values).filter(e -> e.getId().equals(id)).findFirst();
	}

}