package arch.galaxyeclipse.client;

import org.apache.log4j.*;

import arch.galaxyeclipse.client.window.*;

public class GalaxyEclipseClient  {		
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		ClientWindow.getInstance();		
	}
}			