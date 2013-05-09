package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.context.ContextHolder;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class GalaxyEclipseClient  {
    public GalaxyEclipseClient() {

	}
	
	public void start() {
        StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
        ContextHolder.INSTANCE.getClass();

        if (log.isInfoEnabled()) {
            log.info("Client initialized for " + EnvType.CURRENT.toString() + " environment");
        }
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				