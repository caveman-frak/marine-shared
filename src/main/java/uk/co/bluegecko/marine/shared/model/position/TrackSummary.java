package uk.co.bluegecko.marine.shared.model.position;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import uk.co.bluegecko.marine.shared.model.position.partition.LocationPartition;
import uk.co.bluegecko.marine.shared.model.position.partition.Partition;
import uk.co.bluegecko.marine.shared.utility.stream.MergeGatherer;

public class TrackSummary {

	public List<Track> condense(Collection<Track> tracks, Class<? extends Partition<?>> partitionClass) {
		return tracks.stream().map(t -> t.withPartition(partitionClass))
				.filter(Optional::isPresent).map(Optional::get)
				.gather(new MergeGatherer<>())
				.toList();
	}

	public List<Track> condenseRoute(Collection<Track> tracks) {
		return condense(tracks, LocationPartition.class);
	}

}