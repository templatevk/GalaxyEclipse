package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.*;
import java.util.List;

/**
 * OpenGL window delegating drawing to the stage set.
 */
class ClientWindow implements IClientWindow {
    private static final int VIRTUAL_WIDTH = 480;
    private static final int VIRTUAL_HEIGHT = 320;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private static final float PROD_WIDTH = 640;
    private static final float PROD_HEIGHT = 480;

	private IStagePresenter stagePresenter;
	private Rectangle viewport;
	private int viewportHeight;
	private int viewportWidth;
    private List<IDestroyable> destroyables;

	public ClientWindow() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";

        switch (EnvType.CURRENT) {
            case DEV:
                config.width = (int)DEFAULT_WIDTH;
                config.height = (int)DEFAULT_HEIGHT;
                break;
            case PROD:
                config.width = (int)PROD_WIDTH;
                config.height = (int)PROD_HEIGHT;
                config.fullscreen = true;
                break;
        }

		new LwjglApplication(new ClientListener(), config);

        destroyables = new ArrayList<>();
	}

    @Override
    public void setStagePresenter(IStagePresenter stagePresenter) {
        if (this.stagePresenter != null) {
            this.stagePresenter.detach();
        }
        this.stagePresenter = stagePresenter;

        Gdx.input.setInputProcessor(stagePresenter.getGameStage());
    }

    @Override
	public int getViewportHeight() {
		return viewportHeight;
	}

    @Override
	public int getViewportWidth() {
		return viewportWidth;
	}

    @Override
    public void addDestroyable(IDestroyable destroyable) {
        destroyables.add(destroyable);
    }

    private class ClientListener implements ApplicationListener {
        public ClientListener() {

        }

        @Override
        public void create() {
            // Initializing OpenGL
            Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
            Gdx.gl.glEnable(GL10.GL_GENERATE_MIPMAP);
            Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
            Gdx.gl.glEnable(GL10.GL_POINT_SMOOTH);

            Gdx.gl.glHint(GL20.GL_GENERATE_MIPMAP_HINT,         GL20.GL_NICEST);
            Gdx.gl.glHint(GL10.GL_GENERATE_MIPMAP,              GL20.GL_NICEST);
            Gdx.gl.glHint(GL10.GL_LINE_SMOOTH_HINT,             GL20.GL_NICEST);
            Gdx.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,  GL20.GL_NICEST);
            Gdx.gl.glHint(GL10.GL_POINT_SMOOTH_HINT,            GL20.GL_NICEST);
            Gdx.gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT,          GL20.GL_NICEST);
            Gdx.gl.glHint(GL20.GL_FRAGMENT_SHADER,              GL20.GL_NICEST);

            setStagePresenter(StagePresenterFactory.createStagePresenter(
                    StagePresenterFactory.StagePresenterType.MAIN_MENU_PRESENTER));
        }

        @Override
        public void dispose() {
            for (IDestroyable destroyable : destroyables) {
                destroyable.destroy();
            }
            if (stagePresenter != null) {
                stagePresenter.detach();
            }
        }

        @Override
        public void pause() {

        }

        @Override
        public void render() {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glViewport((int)viewport.x, (int)viewport.y,
                    (int)viewport.width, (int)viewport.height);
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            stagePresenter.getGameStage().draw();

            if (EnvType.CURRENT == EnvType.DEV) {
                Table.drawDebug(stagePresenter.getGameStage());
            }
        }

        @Override
        public void resize(int width, int height) {
            // Compute aspect ration, window size and viewport size
            float aspectRatio = (float)width / (float)height;
            float scale = 1f;
            Vector2 crop = new Vector2(0f, 0f);

            if(aspectRatio > ASPECT_RATIO) {
                scale = (float)height/(float)VIRTUAL_HEIGHT;
                crop.x = (width - VIRTUAL_WIDTH * scale)/2f;
            } else if(aspectRatio < ASPECT_RATIO) {
                scale = (float)width/(float)VIRTUAL_WIDTH;
                crop.y = (height - VIRTUAL_HEIGHT * scale)/2f;
            } else {
                scale = (float)width/(float)VIRTUAL_WIDTH;
            }

            float w = (float)VIRTUAL_WIDTH * scale;
            float h = (float)VIRTUAL_HEIGHT * scale;
            viewport = new Rectangle(crop.x, crop.y, w, h);
            viewportWidth = (int)viewport.getWidth();
            viewportHeight = (int)viewport.getHeight();

            stagePresenter.getGameStage().resize(width, height);
        }

        @Override
        public void resume() {

        }
    }
}
