package uk.co.bluegecko.marine.shared.model.position.partition;

import static com.uber.h3core.AreaUnit.rads2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

import com.uber.h3core.util.LatLng;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;
import uk.co.bluegecko.marine.shared.model.compass.Coordinate;

class ByLocationTest extends AbstractPartitionTest {

	SpatialContext ctx;
	ByLocation location;
	private Shape hexagon;

	@BeforeEach
	void setUp() throws IOException {
		setUpCore();
		ctx = JtsSpatialContext.GEO;
		Resolution resolution = Resolution.MEDIUM;
		long cell = h3Core.latLngToCell(0, 0, resolution.h3());
		location = new LocationPartition(resolution, cell);
		hexagon = location.polygon(h3Core, ctx.getShapeFactory());
	}

	@Test
	void location() {
		assertThat(location.cell()).isEqualTo(596538564771053567L);
		assertThat(location.resolution()).isEqualTo(Resolution.MEDIUM);
	}

	@Test
	void polygonCharacteristics() {
		assertThat(hexagon).isInstanceOf(JtsGeometry.class);
		if (hexagon instanceof JtsGeometry jtsGeometry) {
			assertThat(jtsGeometry.hasArea()).isTrue();

			Geometry geometry = jtsGeometry.getGeom();
			assertThat(geometry.getCoordinates()).hasSize(7)
					.containsOnly(
							coord(-0.347497758904720340, 0.044690645952080520),
							coord(-0.149076599292419480, 0.139598563951521500),
							coord(0.019774497642379523, 0.040568958463974200),
							coord(-0.009690978862186525, -0.152694418773190600),
							coord(-0.207421233298126530, -0.247275004831890550),
							coord(-0.376375408881438200, -0.148920082157978030),
							coord(-0.347497758904720340, 0.044690645952080520));
		}
		assertThat(hexagon.getArea(ctx)).as("Area deg²").isEqualTo(0.10665285196520981);
	}

	@Test
	void cellCharacteristics() {
		assertThat(h3Core.cellToBoundary(location.cell())).hasSize(6)
				.containsOnly(
						latLng(-0.347497758904720340, 0.044690645952080520),
						latLng(-0.149076599292419480, 0.139598563951521500),
						latLng(0.019774497642379523, 0.040568958463974200),
						latLng(-0.009690978862186525, -0.152694418773190600),
						latLng(-0.207421233298126530, -0.247275004831890550),
						latLng(-0.376375408881438200, -0.148920082157978030));
		assertThat(h3Core.cellArea(location.cell(), rads2)).as("Area sr (rad²)")
				.isEqualTo(0.00003248837599063693);
	}

	@Test
	void polygonCenter() {
		LatLng center = h3Core.cellToLatLng(location.cell());
		Point expected = ctx.getShapeFactory().pointLatLon(center.lat, center.lng);
		assertThat(hexagon.getCenter().getLat()).as("Latitude").isCloseTo(expected.getLat(), withPercentage(0.1));
		assertThat(hexagon.getCenter().getLon()).as("Longitude").isCloseTo(expected.getLon(), withPercentage(1.0));

	}

	@Test
	void polygonArea() {
		double sq_deg_to_sr = Math.pow(Math.PI, 2) / Math.pow(180, 2);
		assertThat(hexagon.getArea(ctx) * sq_deg_to_sr).as("Area sr (rad²)")
				.isCloseTo(h3Core.cellArea(location.cell(), rads2), withPercentage(0.001));
		assertThat(hexagon.getArea(ctx)).as("Area deg²")
				.isCloseTo(h3Core.cellArea(location.cell(), rads2) / sq_deg_to_sr, withPercentage(0.001));
	}

	@Test
	void contains() {
		assertThat(location.contains(h3Core, ctx.getShapeFactory(),
				new Coordinate(h3Core.cellToLatLng(location.cell())))).isTrue();
	}

	private org.locationtech.jts.geom.Coordinate coord(double lat, double lng) {
		return new org.locationtech.jts.geom.Coordinate(lng, lat);
	}

	private LatLng latLng(double lat, double lng) {
		return new LatLng(lat, lng);
	}

}