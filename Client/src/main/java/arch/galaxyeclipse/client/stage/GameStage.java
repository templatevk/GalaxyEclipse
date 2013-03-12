package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.window.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

public abstract class GameStage extends Stage {	
	protected GameStage() {
		
	}

	protected abstract Group getScaleGroup();
	
	public void resize(int width, int height) {
		setViewport(width, height, true);
		float scaleX = width / ClientWindow.DEFAULT_WIDTH;
		float scaleY = height / ClientWindow.DEFAULT_HEIGHT;
		getScaleGroup().setScale(scaleX, scaleY);
	}
}
