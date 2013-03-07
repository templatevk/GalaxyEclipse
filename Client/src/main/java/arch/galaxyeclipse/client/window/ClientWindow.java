package arch.galaxyeclipse.client.window;

import java.text.*;
import java.util.*;

import org.lwjgl.*;

import arch.galaxyeclipse.client.stage.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;

public class ClientWindow {
	private static final ClientWindow INSTANCE = new ClientWindow();
	
	private class ClientListener implements ApplicationListener {		
		public ClientListener() {

		}
		
		public void create() {
			setStage(new MainMenuStage());
		}

		public void dispose() {
			
		}

		public void pause() {
			
		}

		public void render() {
			  Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		      stage.draw();
		}

		public void resize(int width, int height) {
			stage.setViewport(width, height, true);
		}

		public void resume() {
			
		}		
	}
	
	private LwjglApplication application;
	private Stage stage;
	
	public ClientWindow() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";
		config.width = 640;
		config.height = 480;
		//config.fullscreen = true;
		application = new LwjglApplication(new ClientListener(), config);
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
