package arch.galaxyeclipse.client;

import org.apache.log4j.*;

import arch.galaxyeclipse.shared.inject.*;

public class GalaxyEclipseClient  {		
	public GalaxyEclipseClient() {
		
	}
	
	public void start() {
		PropertyConfigurator.configure("log4j.properties");
		// Instantiating xml context
		SpringContextHolder.CONTEXT.getClass();
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseClient().start();
	}
}				