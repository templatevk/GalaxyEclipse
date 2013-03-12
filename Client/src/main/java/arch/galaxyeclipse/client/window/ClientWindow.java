package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ClientWindow {
	public static final float DEFAULT_WIDTH = 1024;
	public static final float DEFAULT_HEIGHT = 748;

	private static final ClientWindow INSTANCE = new ClientWindow();

	private GameStage stage;

	public ClientWindow() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";
		config.width = (int)DEFAULT_WIDTH;
		config.height = (int)DEFAULT_HEIGHT;
		//config.fullscreen = true;
		new LwjglApplication(new ClientListener(), config);
	}

	public static ClientWindow getInstance() {
		return INSTANCE;
	}

	public void setGameStage(GameStage stage) {
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);
	}

	public GameStage getGameStage() {
		return stage;
	}

	private class ClientListener implements ApplicationListener {
		public ClientListener() {

		}

		@Override
		public void create() {
			setGameStage(new MainMenuStage());
		}

		@Override
		public void dispose() {

		}

		@Override
		public void pause() {

		}	

		@Override
		public void render() {
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			stage.draw();
			Table.drawDebug(stage);
		}

		@Override
		public void resize(int width, int height) {
			stage.resize(width, height);
		}

		@Override
		public void resume() {

		}
	}
}
