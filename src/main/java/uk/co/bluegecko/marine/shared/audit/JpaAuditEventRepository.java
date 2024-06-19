package uk.co.bluegecko.marine.shared.audit;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.bluegecko.marine.shared.audit.data.AuditMapper;
import uk.co.bluegecko.marine.shared.audit.data.AuditRepository;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JpaAuditEventRepository implements AuditEventRepository {

	AuditRepository repository;
	AuditMapper mapper;

	@Transactional
	@Override
	public void add(AuditEvent event) {
		repository.save(mapper.fromApi(event));
	}

	@Transactional
	@Override
	public List<AuditEvent> find(String principal, Instant after, String type) {
		return repository.findAll().stream().map(mapper::toApi).toList();
	}

}