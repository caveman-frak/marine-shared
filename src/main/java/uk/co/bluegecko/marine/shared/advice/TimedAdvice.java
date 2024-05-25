package uk.co.bluegecko.marine.shared.advice;

import java.time.Clock;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Value
@Slf4j
public class TimedAdvice {

	Clock clock;

	@Around(value = "@annotation(timed)")
	public Object time(ProceedingJoinPoint joinPoint, Timed timed) throws Throwable {
		long start = clock.millis();
		try {
			return joinPoint.proceed();
		} finally {
			long end = clock.millis();
			long duration = end - start;
			log.atLevel(timed.level()).log("Timing for {} was {}ms", joinPoint.getSignature(), duration);
			if (timed.print()) {
				System.out.printf("Timing for %s was %dms\n", joinPoint.getSignature(), duration);
			}
		}
	}
}