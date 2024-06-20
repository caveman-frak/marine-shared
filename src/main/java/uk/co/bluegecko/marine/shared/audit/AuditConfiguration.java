package uk.co.bluegecko.marine.shared.audit;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.bluegecko.marine.shared.audit.data.AuditMapper;
import uk.co.bluegecko.marine.shared.audit.data.AuditRepository;

@Configuration
public class AuditConfiguration {

	@Bean
	public AuditEventRepository auditEventRepository(@Autowired(required = false) AuditRepository auditRepository,
			AuditMapper auditMapper, Clock clock) {
		return auditRepository != null ?
				new JpaAuditEventRepository(auditRepository, auditMapper, clock) :
				new InMemoryAuditEventRepository();
	}

	@Bean
	public HttpExchangeRepository httpExchangeRepository() {
		return new InMemoryHttpExchangeRepository();
	}

}