package uk.co.bluegecko.marine.shared.utility;

public interface Ordered<T extends Ordered<T, I>, I extends Comparable<I>> extends Identified<I>, Comparable<T> {

	@Override
	default int compareTo(T other) {
		return getId().compareTo(other.getId());
	}

}