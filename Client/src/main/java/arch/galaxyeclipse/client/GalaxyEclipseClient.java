package arch.galaxyeclipse.client;

import arch.galaxyeclipse.shared.context.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import org.slf4j.*;

public class GalaxyEclipseClient  {		
	public GalaxyEclipseClient() {

	}
	
	public void start() {
        StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
		ContextHolder.INSTANCE.getClass();
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				