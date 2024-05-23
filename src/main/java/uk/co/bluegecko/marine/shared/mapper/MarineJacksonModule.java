package uk.co.bluegecko.marine.shared.mapper;

import javax.measure.Quantity;
import tech.uom.lib.jackson.UnitJacksonModule;

public class MarineJacksonModule extends UnitJacksonModule {


	public MarineJacksonModule() {
		this(Mode.UCUM);
	}

	/**
	 * @param mode the serialization-mode
	 * @since 2.0.2
	 */
	public MarineJacksonModule(Mode mode) {
		super(mode);

		addSerializer(Quantity.class, new QuantityJsonSerializer());
		addDeserializer(Quantity.class, new QuantityJsonDeserializer());
	}
}