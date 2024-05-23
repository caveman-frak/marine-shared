package uk.co.bluegecko.marine.shared.utility.enums;

public interface Codified<T extends Enum<T>> {

	String getCode();

	T fromCode(String code);

}