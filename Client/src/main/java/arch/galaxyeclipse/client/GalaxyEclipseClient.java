package arch.galaxyeclipse.client;

import org.apache.log4j.*;

import arch.galaxyeclipse.shared.context.*;

public class GalaxyEclipseClient  {		
	public GalaxyEclipseClient() {
		
	}
	
	public void start() {
		PropertyConfigurator.configure("log4j.properties");
		// Instantiating xml context
		ContextHolder.INSTANCE.getClass();
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				