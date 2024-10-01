package uk.co.bluegecko.marine.shared.utility;

public class Fluent {

	public interface Identified<T extends Comparable<T>> extends uk.co.bluegecko.marine.shared.utility.Identified<T> {

		T id();

		@Override
		default T getId() {
			return id();
		}

	}

	public interface Codified extends uk.co.bluegecko.marine.shared.utility.Codified {

		String code();

		@Override
		default String getCode() {
			return code();
		}

	}

	public interface Named extends uk.co.bluegecko.marine.shared.utility.Named {

		String name();

		@Override
		default String getName() {
			return name();
		}

	}

	public interface Described extends uk.co.bluegecko.marine.shared.utility.Described {

		String description();

		@Override
		default String getDescription() {
			return description();
		}

	}

}