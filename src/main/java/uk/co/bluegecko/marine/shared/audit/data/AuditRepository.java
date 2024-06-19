package uk.co.bluegecko.marine.shared.audit.data;

import java.util.UUID;
import uk.co.bluegecko.marine.shared.data.repository.WriteOnlyRepository;

public interface AuditRepository extends WriteOnlyRepository<Audit, UUID> {

}