package arch.galaxyeclipse.client.window;

import org.springframework.stereotype.*;

import arch.galaxyeclipse.client.stage.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;

/**
 * ClientWindow - singleton отвечающий за LwjglApplication и делегирующий отрисовку
 * текущей GameStage.
 */
@Component
public class ClientWindow {
	private static final int VIRTUAL_WIDTH = 480;
    private static final int VIRTUAL_HEIGHT = 320;
	
	public static final float DEFAULT_WIDTH = 1024;
	public static final float DEFAULT_HEIGHT = 748;
	public static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;

	private AbstractGameStage stage;
	private Rectangle viewport;
	private int viewportHeight;
	private int viewportWidth;

	public ClientWindow() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";
		config.width = (int)DEFAULT_WIDTH;
		config.height = (int)DEFAULT_HEIGHT;
		//config.fullscreen = true;
		new LwjglApplication(new ClientListener(), config);
	}

	public void setGameStage(AbstractGameStage stage) {
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);
	}

	public AbstractGameStage getGameStage() {
		return stage;
	}

	private class ClientListener implements ApplicationListener {
		public ClientListener() {

		}

		@Override
		public void create() {
			Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
			Gdx.gl.glEnable(GL10.GL_GENERATE_MIPMAP);
			Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
			Gdx.gl.glEnable(GL10.GL_POINT_SMOOTH);
			Gdx.gl.glHint(GL20.GL_GENERATE_MIPMAP_HINT, GL20.GL_NICEST);
			Gdx.gl.glHint(GL10.GL_GENERATE_MIPMAP, GL20.GL_NICEST);
			Gdx.gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL20.GL_NICEST);
			Gdx.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL20.GL_NICEST);
			Gdx.gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL20.GL_NICEST);
			Gdx.gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL20.GL_NICEST);
			Gdx.gl.glHint(GL20.GL_FRAGMENT_SHADER, GL20.GL_NICEST);
			
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
			Gdx.gl.glViewport((int)viewport.x, (int)viewport.y, 
					(int)viewport.width, (int)viewport.height);
//			Gdx.gl.glViewport((int)viewport.x, (int)viewport.y, 
//					Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			stage.draw();
			//Table.drawDebug(stage);
		}

		@Override
		public void resize(int width, int height) {
			float aspectRatio = (float)width / (float)height;
	        float scale = 1f;
	        Vector2 crop = new Vector2(0f, 0f);
			
			if(aspectRatio > ASPECT_RATIO) {
	            scale = (float)height/(float)VIRTUAL_HEIGHT;
	            crop.x = (width - VIRTUAL_WIDTH * scale)/2f;
	        } else if(aspectRatio < ASPECT_RATIO) {
	            scale = (float)width/(float)VIRTUAL_WIDTH;
	            crop.y = (height - VIRTUAL_HEIGHT * scale)/2f;
	        } else {
	            scale = (float)width/(float)VIRTUAL_WIDTH;
	        }
	 
	        float w = (float)VIRTUAL_WIDTH * scale;
	        float h = (float)VIRTUAL_HEIGHT * scale;
	        viewport = new Rectangle(crop.x, crop.y, w, h);
			viewportWidth = (int)viewport.getWidth();
			viewportHeight = (int)viewport.getHeight();  			
	        
			stage.resize(width, height);
		}

		@Override
		public void resume() {

		}
	}

	public int getViewportHeight() {
		return viewportHeight;
	}

	public int getViewportWidth() {
		return viewportWidth;
	}
}
