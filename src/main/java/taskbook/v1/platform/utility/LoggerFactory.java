package taskbook.v1.platform.utility;

import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

import taskbook.v1.platform.annotations.GuardedBy;

@ApplicationScoped
public class LoggerFactory {
	
	@GuardedBy(guardian = "itself")
	private final ConcurrentHashMap<String, Logger> loggers = new ConcurrentHashMap<>();
	
	@Produces @taskbook.v1.platform.annotations.Logger
	public Logger injectLogger(final InjectionPoint injectionPoint) {
		String ip = injectionPoint
				.getMember()
				.getDeclaringClass()
				.getCanonicalName();
		loggers.putIfAbsent(ip, Logger.getLogger(ip));
		return loggers.get(ip);
	}
}
