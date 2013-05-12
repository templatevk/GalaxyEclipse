package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.client.ui.provider.StageProviderFactory;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.common.IDestroyable;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.thread.GeExecutor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class ClientWindow implements IClientWindow {
    private static final float VIRTUAL_WIDTH    = 4;
    private static final float VIRTUAL_HEIGHT   = 3;
    private static final float ASPECT_RATIO     = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;

	private IStageProvider stageProvider;
    private Rectangle viewport;
    private List<IDestroyable> destroyables;

    private @Getter float width;
    private @Getter float height;
    private @Getter float viewportWidth;
    private @Getter float viewportHeight;

    public ClientWindow() {
        this.destroyables = new ArrayList<>();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DisplayMode desktopDisplayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();

        if (log.isInfoEnabled()) {
            log.info("Setting display mode width " + desktopDisplayMode.width
                    + "height " + desktopDisplayMode.height);
        }
        config.title = "Galaxy Eclipse";
        switch (EnvType.CURRENT) {
            case DEV:
                final int DEV_MODE_WIDTH = 800;
                final int DEV_MODE_HEIGHT = (int)(DEV_MODE_WIDTH / ASPECT_RATIO);
                config.width = DEV_MODE_WIDTH;
                config.height = DEV_MODE_HEIGHT;
                break;
            case DEV_UI:
                config.width = (int)DEFAULT_WIDTH;
                config.height = (int)DEFAULT_HEIGHT;
                break;
            case PROD:
                config.width = desktopDisplayMode.width;
                config.height = desktopDisplayMode.height;
                config.fullscreen = true;
                break;
        }
		new LwjglApplication(new ClientListener(), config);
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
        @Override
        public void create() {
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
            glClear();

            setStageProvider(StageProviderFactory.createStageProvider(
                    StageProviderFactory.StagePresenterType.MAIN_MENU));
        }

        @Override
        public void dispose() {
            for (IDestroyable destroyable : destroyables) {
                destroyable.destroy();
            }
            if (stageProvider != null) {
                stageProvider.detach();
            }
            ContextHolder.getBean(GeExecutor.class).shutdownNow();
        }

        @Override
        public void pause() {

        }

        @Override
        public void render() {
            glClear();

            stageProvider.getGameStage().act(Gdx.graphics.getDeltaTime());
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

            if (aspectRatio > ASPECT_RATIO) {
                scale = height / VIRTUAL_HEIGHT;
                crop.x = (width - VIRTUAL_WIDTH * scale) / 2f;
            } else if(aspectRatio < ASPECT_RATIO) {
                scale = width / VIRTUAL_WIDTH;
                crop.y = (height - VIRTUAL_HEIGHT * scale) / 2f;
            } else {
                scale = width / VIRTUAL_WIDTH;
            }

            viewportWidth = VIRTUAL_WIDTH * scale;
            viewportHeight = VIRTUAL_HEIGHT * scale;
            viewport = new Rectangle(crop.x, crop.y, viewportWidth, viewportHeight);

            stageProvider.getGameStage().resize(viewportWidth, viewportHeight);
        }

        @Override
        public void resume() {

        }

        private void glClear() {
            if (EnvType.CURRENT == EnvType.PROD) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
            } else {
                Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            }
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }
    }
}
