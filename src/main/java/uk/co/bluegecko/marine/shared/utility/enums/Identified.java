package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Identified<T extends Enum<T>, I extends Comparable<I>> extends
		uk.co.bluegecko.marine.shared.utility.Identified<I> {

	static <I extends Number & Comparable<I>, T extends Identified<?, I>> Optional<T> fromOffset(
			T[] values, I index, int offset) {
		int pos = index.intValue() - offset;
		return pos >= 0 && pos < values.length ? Optional.of(values[pos]) : Optional.empty();
	}

	static <I extends Comparable<I>, T extends Identified<?, I>> Optional<T> fromId(T[] values, I id) {
		return Arrays.stream(values).filter(e -> e.getId().equals(id)).findFirst();
	}

}