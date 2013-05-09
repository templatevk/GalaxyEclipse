package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.IButtonBuilder;
import arch.galaxyeclipse.client.ui.IButtonClickCommand;
import arch.galaxyeclipse.client.ui.StageUiFactory;
import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.client.ui.provider.StageProviderFactory;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.util.IDestroyable;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class ClientWindow implements IClientWindow {
    private static final float VIRTUAL_WIDTH = 480;
    private static final float VIRTUAL_HEIGHT = 360;
    private static final float ASPECT_RATIO = VIRTUAL_WIDTH / VIRTUAL_HEIGHT;
    private static final float PROD_WIDTH = 640;
    private static final float PROD_HEIGHT = 480;

	private IStageProvider stageProvider;
    private Rectangle viewport;
    private List<IDestroyable> destroyables;

    private @Getter float width;
    private @Getter float height;

	public ClientWindow() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy Eclipse";

        switch (EnvType.CURRENT) {
//            TODO switch to below commented case
            case PROD:
            case DEV:
                config.width = (int)DEFAULT_WIDTH;
                config.height = (int)DEFAULT_HEIGHT;
                break;
//            case PROD:
//                config.width = (int)PROD_WIDTH;
//                config.height = (int)PROD_HEIGHT;
//                config.fullscreen = true;
//                break;
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

        if (EnvType.CURRENT == EnvType.DEV) {
            TextButton mainMenuBtn = StageUiFactory.createButtonBuilder()
                    .setText("Main menu")
                    .setType(IButtonBuilder.ButtonType.MainMenuButton)
                    .setClickCommand(new IButtonClickCommand() {
                        @Override
                        public void execute(InputEvent e, float x, float y) {
                            IStageProvider provider = StageProviderFactory.createStageProvider(
                                    StageProviderFactory.StagePresenterType.MAIN_MENU);
                            setStageProvider(provider);
                            provider.getGameStage().forceResize();
                        }
                    }).build();

            stageProvider.getGameStage().addActor(mainMenuBtn);
        }
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
        }

        @Override
        public void pause() {

        }

        @Override
        public void render() {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glViewport((int)viewport.x, (int)viewport.y,
                    (int)viewport.width, (int)viewport.height);

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

            float viewportWidth = VIRTUAL_WIDTH * scale;
            float viewportHeight = VIRTUAL_HEIGHT * scale;
            viewport = new Rectangle(crop.x, crop.y, viewportWidth, viewportHeight);

            stageProvider.getGameStage().resize(viewportWidth, viewportHeight);
        }

        @Override
        public void resume() {

        }
    }
}
