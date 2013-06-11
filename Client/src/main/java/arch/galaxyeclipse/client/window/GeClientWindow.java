package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.client.ui.provider.StageProviderFactory;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.common.IDestroyable;
import arch.galaxyeclipse.shared.thread.GeExecutor;
import arch.galaxyeclipse.shared.thread.TaskRunnablePair;
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

import static arch.galaxyeclipse.shared.context.GeContextHolder.getBean;
import static com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration.getDesktopDisplayMode;

@Slf4j
class GeClientWindow implements IGeClientWindow {
    public static final int RENDER_REQUEST_MILLISECONDS_DELAY = 50;

    private static final float VIRTUAL_WIDTH    = 4;
    private static final float VIRTUAL_HEIGHT   = 3;
    private static final float ASPECT_RATIO     = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;

    private GeTaskRunnablePair<RenderRequestRunnable> renderRequestTaskRunnablePair;
    private IGeStageProvider stageProvider;
    private List<IGeDisposable> disposables;

    private @Getter Rectangle viewport;
    private @Getter float width;
    private @Getter float height;
    private @Getter float viewportWidth;
    private @Getter float viewportHeight;
    private @Getter float stateTime;

    public GeClientWindow() {
        disposables = new ArrayList<>();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DisplayMode desktopDisplayMode = getDesktopDisplayMode();

        if (GeClientWindow.log.isInfoEnabled()) {
            GeClientWindow.log.info("Setting display mode width " + desktopDisplayMode.width
                    + " height " + desktopDisplayMode.height);
        }
        config.title = "Galaxy Eclipse";
        switch (GeEnvType.CURRENT) {
            case DEV:
                final int DEV_MODE_WIDTH = 800;
                final int DEV_MODE_HEIGHT = (int)(DEV_MODE_WIDTH / ASPECT_RATIO);
                config.width = DEV_MODE_WIDTH;
                config.height = DEV_MODE_HEIGHT;
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
    public void setStageProvider(IGeStageProvider stageProvider) {
        if (this.stageProvider != null) {
            this.stageProvider.detach();
        }
        this.stageProvider = stageProvider;

        Gdx.input.setInputProcessor(stageProvider.getGameStage());
    }

    @Override
    public void addDestroyable(IGeDisposable destroyable) {
        disposables.add(destroyable);
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
            Gdx.graphics.setContinuousRendering(false);
            glClear();

            if (GeEnvType.CURRENT == GeEnvType.PROD) {
                Gdx.input.setCursorCatched(true);
            }

            setStageProvider(GeStageProviderFactory.createStageProvider(
                    StageProviderType.MAIN_MENU));

            renderRequestTaskRunnablePair = new GeTaskRunnablePair<>(
                    RENDER_REQUEST_MILLISECONDS_DELAY, new RenderRequestRunnable(),
                    true, false);
            renderRequestTaskRunnablePair.start();
        }

        @Override
        public void dispose() {
            for (IGeDisposable destroyable : disposables) {
                destroyable.dispose();
            }
            if (stageProvider != null) {
                stageProvider.detach();
            }
            getBean(GeExecutor.class).shutdownNow();
        }

        @Override
        public void pause() {

        }

        @Override
        public void render() {
            glClear();

            float deltaTime = Gdx.graphics.getDeltaTime();
            stateTime += deltaTime;

            stageProvider.getGameStage().act(deltaTime);
            stageProvider.getGameStage().draw();

            if (GeEnvType.CURRENT == GeEnvType.DEV) {
                Table.drawDebug(stageProvider.getGameStage());
            }
        }

        @Override
        public void resize(int width, int height) {
            GeClientWindow.this.width = width;
            GeClientWindow.this.height = height;

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
            if (GeEnvType.CURRENT == GeEnvType.PROD) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
            } else {
                Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            }
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }
    }

    private class RenderRequestRunnable implements Runnable {
        @Override
        public void run() {
            Gdx.graphics.requestRendering();
            if (GeClientWindow.log.isTraceEnabled()) {
                GeClientWindow.log.trace("Client rendering request");
            }
        }
    }
}
