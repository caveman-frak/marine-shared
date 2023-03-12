package uk.co.bluegecko.marine.shared.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import tech.units.indriya.format.SimpleQuantityFormat;

import javax.measure.Quantity;
import java.io.IOException;

/**
 * Serialize {@link Quantity} using the {@link SimpleQuantityFormat}.
 */
@SuppressWarnings("rawtypes")
class QuantityJsonSerializer extends StdScalarSerializer<Quantity> {

	public QuantityJsonSerializer() {
		super(Quantity.class);
	}

	@Override
	public void serialize(Quantity value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeObject(SimpleQuantityFormat.getInstance().format(value));
	}

}
