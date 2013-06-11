package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.context.GeContextHolder;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import static arch.galaxyeclipse.shared.GeEnvType.CURRENT;

@Slf4j
public class GeClient {
    public GeClient() {

	}
	
	public void start() {
        StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
        GeContextHolder.INSTANCE.getClass();

        if (GeClient.log.isInfoEnabled()) {
            GeClient.log.info("Client initialized for " + CURRENT.toString() + " environment");
        }
	}
	
	public static void main(String[] args) throws Exception {
		new GeClient().start();
	}
}				