package uk.co.bluegecko.marine.shared.utility.enums;

import java.util.Arrays;
import java.util.Optional;

public interface Ordered<T extends Enum<T> & Ordered<T, I>, I extends Comparable<I>>
		extends uk.co.bluegecko.marine.shared.utility.Ordered<T, I> {

	static <I extends Comparable<I>, T extends Ordered<?, I>> Optional<T> fromId(T[] values, I id) {
		return Arrays.stream(values).filter(e -> e.getId().equals(id)).findFirst();
	}


}