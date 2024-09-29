package uk.co.bluegecko.marine.shared.model.compass;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Offset;
import uk.co.bluegecko.marine.shared.model.quantity.QuantityConditions;

@SuppressWarnings({"rawtypes"})
public class CompassConditions {

	public static Condition<Quantity> isCloseTo(Quantity<Angle> expected, Offset<Double> offset) {
		return QuantityConditions.isCloseTo(expected, offset);
	}

}