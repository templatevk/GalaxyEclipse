package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import com.badlogic.gdx.graphics.g2d.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;

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