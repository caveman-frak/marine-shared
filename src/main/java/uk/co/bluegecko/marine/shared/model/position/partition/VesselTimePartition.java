package uk.co.bluegecko.marine.shared.model.position.partition;

import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.CompareToBuilder;


public record VesselTimePartition(Resolution resolution, UUID vessel, long epochIntervals)
		implements Partition<VesselTimePartition>, ByVessel, ByTime {

	@Override
	public int compareTo(VesselTimePartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(vessel, o.vessel)
				.append(epochIntervals, o.epochIntervals)
				.build();
	}

	public static Partitioner partitioner() {
		return (r, t) -> new VesselTimePartition(r, t.getVesselId(), r.epochIntervals(t.getTimestamp()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if ((p instanceof ByTime pT) && (p instanceof ByVessel pV)) {
			return Optional.of(new VesselTimePartition(p.resolution(), pV.vessel(), pT.epochIntervals()));
		}
		return Optional.empty();
	}

}