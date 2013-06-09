package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class CursorWidget {

    private static final float FRAME_DURATION = 0.12f;

    private static final String[] FRAMES = {
        "ui/cursor/cursor1", "ui/cursor/cursor2", "ui/cursor/cursor3",
        "ui/cursor/cursor4", "ui/cursor/cursor5", "ui/cursor/cursor6",
        "ui/cursor/cursor7", "ui/cursor/cursor8", "ui/cursor/cursor9",
        "ui/cursor/cursor10"
    };

    private IClientWindow clientWindow;

    private Animation animation;

    public CursorWidget() {
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        Array<TextureRegion> cursorRegions = new Array<>(FRAMES.length);
        for (int i = 0; i < FRAMES.length; i++) {
            AtlasRegion region = resourceLoader.findRegion(FRAMES[i]);
            cursorRegions.add(region);
        }

        clientWindow = ContextHolder.getBean(IClientWindow.class);
        animation = new Animation(FRAME_DURATION, cursorRegions, Animation.LOOP);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = animation.getKeyFrame(clientWindow.getStateTime());
        int cursorHeight = frame.getRegionHeight();
        int y = (int)clientWindow.getHeight() - Gdx.input.getY() - cursorHeight;
        int x = Gdx.input.getX();
        int clientWidth = (int)clientWindow.getWidth();
        int clientHeight = (int)clientWindow.getHeight();

        if (x < 0) {
            x = 0;
        } else if (x > clientWidth) {
            x = clientWidth;
        }
        if (y + cursorHeight < 0) {
            y = -cursorHeight;
        } else if (y + cursorHeight > clientHeight) {
            y = clientHeight - cursorHeight;
        }

        Gdx.input.setCursorPosition(x, y + cursorHeight);
        batch.draw(frame, x, y);
    }
}
