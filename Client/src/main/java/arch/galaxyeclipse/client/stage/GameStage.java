package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.extern.slf4j.*;

/**
 * Base class for all game stages. Performs scaling, resizing and aspect ration matching.
 * Stages are obtained via the getStage method.
 */
@Slf4j
public abstract class GameStage extends Stage {
    private static final float DEFAULT_SCALE_COEF = 0.5f;

    private IClientWindow clientWindow;

    protected GameStage() {
        clientWindow = ContextHolder.getBean(IClientWindow.class);
    }

    // Root container we are going to scale if isManualScaling returns false
    protected abstract Group getScaleGroup();

    // Override in derived classes to notify about manual scaling of the stage
    protected boolean isManualScaling() {
        return false;
    }

    public void resize(int width, int height) {
        setViewport(width, height, true);
        float scaleX = width / IClientWindow.DEFAULT_WIDTH;
        float scaleY = height / IClientWindow.DEFAULT_HEIGHT;

        if (!isManualScaling()) {
            getScaleGroup().setScale(scaleX * DEFAULT_SCALE_COEF, scaleY * DEFAULT_SCALE_COEF);
            if (GameStage.log.isInfoEnabled()) {
                GameStage.log.info("scaleX = " + scaleX * DEFAULT_SCALE_COEF
                        + ", scaleY = " + scaleY * DEFAULT_SCALE_COEF);
            }
        }

        if (GameStage.log.isInfoEnabled()) {
            GameStage.log.info("width = " + width + ", height = " + height + ", viewportHeight = "
                    + clientWindow.getViewportHeight() + ", viewportWidth = "
                    + clientWindow.getViewportWidth());
        }
    }
}