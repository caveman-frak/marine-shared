package uk.co.bluegecko.marine.shared.audit.data;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import uk.co.bluegecko.marine.shared.data.repository.WriteOnlyRepository;

public interface AuditRepository extends WriteOnlyRepository<Audit, UUID> {

	Stream<Audit> findByCreatedBetweenOrderByCreatedDesc(Instant start, Instant end);

	Stream<Audit> findByPrincipalAndCreatedBetweenOrderByCreatedDesc(String principal, Instant start, Instant end);

	Stream<Audit> findByTypeAndCreatedBetweenOrderByCreatedDesc(String type, Instant start, Instant end);

}