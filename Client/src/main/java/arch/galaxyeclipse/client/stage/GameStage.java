package arch.galaxyeclipse.client.stage;

import org.apache.log4j.*;

import arch.galaxyeclipse.client.window.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

public abstract class GameStage extends Stage {
	private static final Logger log = Logger.getLogger(GameStage.class);
	
	private static final float DEFAULT_SCALE_COEF = 0.5f;
	
	protected GameStage() {
		
	}

	protected abstract Group getScaleGroup();
	
	public void resize(int width, int height) {
		setViewport(width, height, true);
		float scaleX = width / ClientWindow.DEFAULT_WIDTH;
		float scaleY = height / ClientWindow.DEFAULT_HEIGHT;
		
		getScaleGroup().setScale(scaleX * DEFAULT_SCALE_COEF, scaleY * DEFAULT_SCALE_COEF);		
		log.info("scaleX = " + scaleX * DEFAULT_SCALE_COEF
				+ ", scaleY = " + scaleY * DEFAULT_SCALE_COEF);
	}
}
