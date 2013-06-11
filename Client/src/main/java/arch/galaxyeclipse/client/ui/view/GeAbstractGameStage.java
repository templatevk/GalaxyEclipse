package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.resource.IGeResourceLoader;
import arch.galaxyeclipse.client.ui.widget.GeCursorWidget;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GeAbstractGameStage extends Stage {

    private static final float DEFAULT_SCALE_COEF = 0.5f;

    private IGeResourceLoader resourceLoader;
    private GeCursorWidget cursorWidget;

    private
    @Getter(AccessLevel.PROTECTED)
    IGeClientWindow clientWindow;
    private
    @Getter(AccessLevel.PROTECTED)
    float scaleX;
    private
    @Getter(AccessLevel.PROTECTED)
    float scaleY;

    protected GeAbstractGameStage() {
        clientWindow = GeContextHolder.getBean(IGeClientWindow.class);

        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        cursorWidget = GeContextHolder.getBean(GeCursorWidget.class);
    }

    public void resize(float viewportWidth, float viewportHeight) {
        setViewport(viewportWidth, viewportHeight, true);

        scaleX = viewportWidth / IGeClientWindow.DEFAULT_WIDTH * DEFAULT_SCALE_COEF;
        scaleY = viewportHeight / IGeClientWindow.DEFAULT_HEIGHT * DEFAULT_SCALE_COEF;

        if (!isManualScaling()) {
            getScaleGroup().setScale(scaleX, scaleY);
            if (GeAbstractGameStage.log.isInfoEnabled()) {
                GeAbstractGameStage.log.info("scaleX = " + scaleX + ", scaleY = " + scaleY);
            }
        }

        if (GeAbstractGameStage.log.isInfoEnabled()) {
            GeAbstractGameStage.log.info("width = " + clientWindow.getWidth()
                    + ", height = " + clientWindow.getHeight()
                    + ", viewportWidth = " + viewportWidth
                    + ", viewportHeight = " + viewportHeight);
        }
    }

    @Override
    public void draw() {
        float viewportWidth = getClientWindow().getViewportWidth();
        float viewportHeight = getClientWindow().getViewportHeight();
        float width = getClientWindow().getWidth();
        float height = getClientWindow().getHeight();

        Rectangle area = new Rectangle(
                (width - viewportWidth) / 2f,
                (height - viewportHeight) / 2f,
                viewportWidth, viewportHeight);
        Rectangle scissor = new Rectangle();
        ScissorStack.calculateScissors(getCamera(), getSpriteBatch().getTransformMatrix(),
                area, scissor);
        ScissorStack.pushScissors(scissor);
        super.draw();
        ScissorStack.popScissors();

        getSpriteBatch().begin();
        cursorWidget.draw(getSpriteBatch());
        getSpriteBatch().end();
    }

    public void forceResize() {
        resize(clientWindow.getViewportWidth(), clientWindow.getViewportHeight());
    }

    protected Group getScaleGroup() {
        return null;
    }

    protected boolean isManualScaling() {
        return false;
    }
}