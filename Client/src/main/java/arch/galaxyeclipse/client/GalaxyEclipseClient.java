package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;

@Slf4j
public class GalaxyEclipseClient  {
    @Getter
    private static EnvType envType;

    public GalaxyEclipseClient() {

	}
	
	public void start() {
        StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
        ContextHolder.class.getClass();

        envType = SharedInfo.getEnvType();
        if (log.isInfoEnabled()) {
            log.info("Client initialized for " + envType.toString() + " environment");
        }
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				