package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.inject.*;
import com.badlogic.gdx.scenes.scene2d.*;
import org.apache.log4j.*;

/**
 * Base class for all game stages. Performs scaling, resizing and aspect ration matching.
 * Stages are obtained via the getStage method.
 */
public abstract class AbstractGameStage extends Stage {
    public enum StageType {
        LOADING,
        MAIN_MENU,
        FLIGHT,
        STATION;
    }

    private static final Logger log = Logger.getLogger(AbstractGameStage.class);

    private static final float DEFAULT_SCALE_COEF = 0.5f;

    private ClientWindow clientWindow;

    protected AbstractGameStage() {
        clientWindow = SpringContextHolder.CONTEXT.getBean(ClientWindow.class);
    }

    public static AbstractGameStage getStage(StageType stageType) {
        switch (stageType) {
            case LOADING:
                return new LoadingStage();
            case MAIN_MENU:
                return new MainMenuStage();
            case FLIGHT:
                return new FlightStage();
            case STATION:
                return new StationStage();
        }
        return null;
    }

    // Root container we are going to scale if isManualScaling returns false
    protected abstract Group getScaleGroup();

    // Override in derived classes to notify about manual scaling of the stage
    protected boolean isManualScaling() {
        return false;
    }

    public void resize(int width, int height) {
        setViewport(width, height, true);
        float scaleX = width / ClientWindow.DEFAULT_WIDTH;
        float scaleY = height / ClientWindow.DEFAULT_HEIGHT;

        if (!isManualScaling()) {
            getScaleGroup().setScale(scaleX * DEFAULT_SCALE_COEF, scaleY * DEFAULT_SCALE_COEF);
            log.info("scaleX = " + scaleX * DEFAULT_SCALE_COEF
                    + ", scaleY = " + scaleY * DEFAULT_SCALE_COEF);
        }
        log.info("width = " + width + ", height = " + height + ", viewportHeight = "
                + clientWindow.getViewportHeight() + ", viewportWidth = "
                + clientWindow.getViewportWidth());
    }
}