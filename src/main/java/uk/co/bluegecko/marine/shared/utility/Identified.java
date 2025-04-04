package uk.co.bluegecko.marine.shared.utility;

import java.util.Optional;
import java.util.stream.Stream;

public interface Identified<I> {

	I getId();

	static <T extends Identified<I>, I> Optional<T> fromId(Stream<T> values, I id) {
		return values.filter(e -> e.getId().equals(id)).findFirst();
	}

}