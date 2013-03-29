package arch.galaxyeclipse.client.stage;

import org.apache.log4j.*;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.inject.*;

import com.badlogic.gdx.scenes.scene2d.*;

/**
 * Базовый класс всех сцен реализующий масштабирование элементов сцены 
 * и установку размера Viewport'a. Stage состоит из многих Image/Actor которые 
 * представляют елементы UI или игровые объекты.
 *
 */
public abstract class AbstractGameStage extends Stage implements IStage {
	private static final Logger log = Logger.getLogger(AbstractGameStage.class);
	
	private static final float DEFAULT_SCALE_COEF = 0.5f;
	
	private ClientWindow clientWindow;
	
	protected AbstractGameStage() {
		clientWindow = SpringContextHolder.CONTEXT.getBean(ClientWindow.class);
	}

	protected abstract Group getScaleGroup();
	
	public void resize(int width, int height) {
		setViewport(width, height, true);
		float scaleX = width / ClientWindow.DEFAULT_WIDTH;
		float scaleY = height / ClientWindow.DEFAULT_HEIGHT;
		
		getScaleGroup().setScale(scaleX * DEFAULT_SCALE_COEF, scaleY * DEFAULT_SCALE_COEF);		
		log.info("scaleX = " + scaleX * DEFAULT_SCALE_COEF
				+ ", scaleY = " + scaleY * DEFAULT_SCALE_COEF);
		log.info("width = " + width + ", height = " + height + ", viewportHeight = "
				+ clientWindow.getViewportHeight() + ", viewportWidth = "
				+ clientWindow.getViewportWidth());
	}
}
