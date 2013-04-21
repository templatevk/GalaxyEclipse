package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.*;

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

	private IStageProvider stageProvider;
    private Rectangle viewport;
    private List<IDestroyable> destroyables;

	private @Getter int viewportHeight;
	private @Getter int viewportWidth;
    private @Getter int width;
    private @Getter int height;

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
    public void setStageProvider(IStageProvider stageProvider) {
        if (this.stageProvider != null) {
            this.stageProvider.detach();
        }
        this.stageProvider = stageProvider;

        Gdx.input.setInputProcessor(stageProvider.getGameStage());
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

            setStageProvider(StageProviderFactory.createStageProvider(
                    StageProviderFactory.StagePresenterType.MAIN_MENU_PROVIDER));
        }

        @Override
        public void dispose() {
            for (IDestroyable destroyable : destroyables) {
                destroyable.destroy();
            }
            if (stageProvider != null) {
                stageProvider.detach();
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

            stageProvider.getGameStage().draw();

            if (EnvType.CURRENT == EnvType.DEV) {
                Table.drawDebug(stageProvider.getGameStage());
            }
        }

        @Override
        public void resize(int width, int height) {
            ClientWindow.this.width = width;
            ClientWindow.this.height = height;

            float aspectRatio = (float)width / (float)height;
            float scale;
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

            stageProvider.getGameStage().resize(width, height);
        }

        @Override
        public void resume() {

        }
    }
}
