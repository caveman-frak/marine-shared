package uk.co.bluegecko.marine.shared.utility;

/**
 * Adds support for the standard interfaces using the fluent accessor style:
 * <br/>{@link uk.co.bluegecko.marine.shared.utility.Identified},
 * <br/>{@link uk.co.bluegecko.marine.shared.utility.Codified}, <br/>{@link uk.co.bluegecko.marine.shared.utility.Named}
 * and <br/>{@link uk.co.bluegecko.marine.shared.utility.Described}.
 */
public class Fluent {

	public interface Identified<T> extends uk.co.bluegecko.marine.shared.utility.Identified<T> {

		T id();

		@Override
		default T getId() {
			return id();
		}

	}

	public interface Ordered<T extends Ordered<T, I>, I extends Comparable<I>>
			extends uk.co.bluegecko.marine.shared.utility.Ordered<T, I>, Identified<I> {

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