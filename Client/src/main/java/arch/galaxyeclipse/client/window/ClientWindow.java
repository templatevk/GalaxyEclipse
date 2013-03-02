package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.scenes.scene2d.*;

public class ClientWindow extends LwjglApplication {
	private static final ClientWindow INSTANCE; 
	
	static {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";
		config.width = 640;
		config.height = 480;
		INSTANCE = new ClientWindow(new ClientListener(), config);
	}
	
	private Stage stage;
	
	public ClientWindow(ApplicationListener listener, LwjglApplicationConfiguration config) {
		super(listener, config);
		stage = new MainMenuStage();
	}
	
	public static void init() {
		
	}
	
	public static ClientWindow getInstance() {
		return INSTANCE;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);
	}

	public Stage getStage() {
		return stage;
	}
}
