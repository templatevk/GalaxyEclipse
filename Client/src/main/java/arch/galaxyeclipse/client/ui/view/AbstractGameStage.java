package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGameStage extends Stage {
    private static final float DEFAULT_SCALE_COEF = 0.5f;

    private @Getter(AccessLevel.PROTECTED) IClientWindow clientWindow;
    private @Getter(AccessLevel.PROTECTED) float scaleX;
    private @Getter(AccessLevel.PROTECTED) float scaleY;

    protected AbstractGameStage() {
        clientWindow = ContextHolder.getBean(IClientWindow.class);
    }

    public void resize(float viewportWidth, float viewportHeight) {
        setViewport(viewportWidth, viewportHeight, false);

        scaleX = viewportWidth / IClientWindow.DEFAULT_WIDTH * DEFAULT_SCALE_COEF;
        scaleY = viewportHeight / IClientWindow.DEFAULT_HEIGHT * DEFAULT_SCALE_COEF;

        if (!isManualScaling()) {
            getScaleGroup().setScale(scaleX, scaleY);
            if (AbstractGameStage.log.isInfoEnabled()) {
                AbstractGameStage.log.info("scaleX = " + scaleX + ", scaleY = " + scaleY);
            }
        }

        if (AbstractGameStage.log.isInfoEnabled()) {
            AbstractGameStage.log.info("width = " + clientWindow.getWidth()
                    + ", height = " + clientWindow.getHeight()
                    + ", viewportWidth = " + viewportWidth
                    + ", viewportHeight = " + viewportHeight);
        }
    }

    public void forceResize() {
        resize(clientWindow.getWidth(), clientWindow.getHeight());
    }

    protected Group getScaleGroup() {
        return null;
    }

    protected boolean isManualScaling() {
        return false;
    }
}