package uk.co.bluegecko.marine.shared.audit;

import static net.logstash.logback.argument.StructuredArguments.kv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingAuditEventListener {

	@EventListener
	public void on(AuditApplicationEvent event) {
		log.info("An Audit Event was received: {}", kv("audit", event));
	}

//	@EventListener
//	public void on(AbstractAuthorizationEvent event) {
//		log.debug("A Security Event was received: {}", kv("audit", event));
//	}

}