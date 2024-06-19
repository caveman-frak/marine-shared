package uk.co.bluegecko.marine.shared.audit;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.bluegecko.marine.shared.audit.data.Audit;
import uk.co.bluegecko.marine.shared.audit.data.AuditRepository;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JpaAuditEventRepository implements AuditEventRepository {

	AuditRepository auditRepository;

	@Transactional
	@Override
	public void add(AuditEvent event) {
		auditRepository.save(Audit.from(event));
	}

	@Transactional
	@Override
	public List<AuditEvent> find(String principal, Instant after, String type) {
		return auditRepository.findAll().stream().map(Audit::toEvent).toList();
	}

}