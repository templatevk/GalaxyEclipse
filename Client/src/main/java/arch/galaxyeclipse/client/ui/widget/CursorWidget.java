package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class CursorWidget {

    private static final float FRAME_DURATION = 100;
    private static final String[] FRAMES = {
        "cursor1", "cursor2", "cursor3", "cursor4", "cursor5",
        "cursor6", "cursor7", "cursor8", "cursor9", "cursor10"
    };

    private IClientWindow clientWindow;

    private Animation animation;

    public CursorWidget() {
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        Array<TextureRegion> cursorRegions = new Array<>(FRAMES.length);
        for (String frame : FRAMES) {
            AtlasRegion region = resourceLoader.findRegion(frame);
            cursorRegions.add(region);
        }

        clientWindow = ContextHolder.getBean(IClientWindow.class);
        animation = new Animation(FRAME_DURATION, cursorRegions, Animation.LOOP);
    }

    public void draw(SpriteBatch batch) {
//        TextureRegion frame = animation.getKeyFrame(clientWindow.getStateTime());
//        batch.draw(frame, Gdx.input.getX(), clientWindow.getHeight() - Gdx.input.getY());
    }
}
