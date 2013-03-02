package arch.galaxyeclipse.client.window;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;

class ClientListener implements ApplicationListener {
	public ClientListener() {

	}
	
	public void create() {
		
	}

	public void dispose() {
		
	}

	public void pause() {
		
	}

	public void render() {
		  Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      ClientWindow.getInstance().getStage().draw();
	}

	public void resize(int width, int height) {
		ClientWindow.getInstance().getStage().setViewport(width, height, true);
	}

	public void resume() {
		
	}		
}