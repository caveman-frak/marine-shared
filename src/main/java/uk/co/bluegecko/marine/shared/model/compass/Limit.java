package uk.co.bluegecko.marine.shared.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;

import java.util.EnumSet;
import java.util.Set;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.quantity.QuantityRange;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
public enum Limit {
	BEARING(range(0, 360), EnumSet.noneOf(Hemisphere.class)),
	LATITUDE(range(-90, 90), EnumSet.of(Hemisphere.NORTH, Hemisphere.SOUTH)),
	LONGITUDE(range(-180, 180), EnumSet.of(Hemisphere.EAST, Hemisphere.WEST)),
	UNBOUND(unbound(), EnumSet.noneOf(Hemisphere.class));

	QuantityRange<Angle> range;
	Set<Hemisphere> hemispheres;

	@SuppressWarnings("unchecked")
	private static QuantityRange<Angle> range(Quantity<Angle> minimum, Quantity<Angle> maxiumum) {
		return QuantityRange.of(minimum, maxiumum);
	}

	private static QuantityRange<Angle> range(double minimum, double maxiumum) {
		return range(Quantities.getQuantity(minimum, DEGREE), Quantities.getQuantity(maxiumum, DEGREE));
	}

	private static QuantityRange<Angle> unbound() {
		return range(null, null);
	}

}