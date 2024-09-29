package uk.co.bluegecko.marine.shared.utility.stream;

import java.util.Optional;

public interface Mergeable<T> {

	Optional<T> merge(T other);

}