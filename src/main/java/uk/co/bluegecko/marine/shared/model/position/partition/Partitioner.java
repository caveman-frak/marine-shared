package uk.co.bluegecko.marine.shared.model.position.partition;

import java.util.function.BiFunction;
import uk.co.bluegecko.marine.shared.model.position.Trace;

public interface Partitioner extends BiFunction<Resolution, Trace, Partition<?>> {

}