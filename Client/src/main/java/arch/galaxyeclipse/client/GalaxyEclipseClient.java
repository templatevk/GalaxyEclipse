package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.context.ContextHolder;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import static arch.galaxyeclipse.shared.EnvType.CURRENT;

@Slf4j
public class GalaxyEclipseClient  {
    public GalaxyEclipseClient() {

	}
	
	public void start() {
        StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
        ContextHolder.INSTANCE.getClass();

        if (log.isInfoEnabled()) {
            log.info("Client initialized for " + CURRENT.toString() + " environment");
        }
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				