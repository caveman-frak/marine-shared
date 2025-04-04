package uk.co.bluegecko.marine.shared.utility.enums;

public interface Ordered<T extends Enum<T> & Ordered<T, I>, I extends Comparable<I>>
		extends uk.co.bluegecko.marine.shared.utility.Ordered<T, I>, Identified<T, I> {

}