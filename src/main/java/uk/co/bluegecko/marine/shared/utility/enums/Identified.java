package uk.co.bluegecko.marine.shared.utility.enums;

public interface Identified<T extends Enum<T>> {

	int getId();

	T fromId(int id);

}