package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class LoadingStageProvider implements IStageProvider {
    private LoadingStage stage;

    LoadingStageProvider() {
        stage = new LoadingStage();
    }

    @Override
    public AbstractGameStage getGameStage() {
        return stage;
    }

    @Override
    public void detach() {

    }

    class LoadingStage extends AbstractGameStage {
        private Table rootTable;
        private Table innerTable;

        LoadingStage() {
            IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);

            innerTable = new Table();
            innerTable.setBounds(0, 0, 400, 200);
            innerTable.setTransform(true);
            innerTable.setBackground(new TextureRegionDrawable(resourceLoader
                    .findRegion("ui/progress")));

            rootTable = new Table();
            rootTable.setFillParent(true);
            rootTable.setTransform(false);
            rootTable.setBackground(new TextureRegionDrawable(resourceLoader
                    .findRegion("ui/menu_login")));
            rootTable.add(innerTable);

            addActor(rootTable);
        }

        @Override
        protected Group getScaleGroup() {
            return innerTable;
        }
    }
}
