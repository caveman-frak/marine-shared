package uk.co.bluegecko.marine.shared.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.units.indriya.format.SimpleQuantityFormat;

import javax.measure.Quantity;
import java.io.IOException;

/**
 * Deserialize {@link Quantity} using the {@link SimpleQuantityFormat}.
 */
@SuppressWarnings("rawtypes")
class QuantityJsonDeserializer extends StdDeserializer<Quantity> {
	public QuantityJsonDeserializer() {
		super(Quantity.class);
	}

	@Override
	public Quantity deserialize(JsonParser parser, DeserializationContext deserializationContext)
			throws IOException {

		return SimpleQuantityFormat.getInstance().parse(parser.getValueAsString());
	}
}
