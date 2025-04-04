package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Identified<T extends Enum<T>, I> extends
		uk.co.bluegecko.marine.shared.utility.Identified<I> {

	static <T extends Enum<T> & Identified<T, I>, I> Optional<T> fromId(T[] values, I id) {
		return uk.co.bluegecko.marine.shared.utility.Identified.fromId(Arrays.stream(values), id);
	}

}