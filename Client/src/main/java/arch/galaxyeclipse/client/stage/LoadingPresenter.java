package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class LoadingPresenter implements IStageProvider {
    private LoadingStage view;

    LoadingPresenter() {
        view = new LoadingStage();
    }

    @Override
    public GameStage getGameStage() {
        return view;
    }

    @Override
    public void detach() {

    }

    private static class LoadingStage extends GameStage {
        private Table rootTable;
        private Table innerTable;

        LoadingStage() {
            IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);

            rootTable = new Table();
            rootTable.setFillParent(true);
            rootTable.setBackground(new TextureRegionDrawable(resourceLoader.findRegion("ui/menu_login")));
            addActor(rootTable);

            innerTable = new Table();
            innerTable.setBounds(0, 0, 400, 200);
            innerTable.setTransform(true);
            rootTable.setTransform(false);
            rootTable.add(innerTable);

        }

        @Override
        protected Group getScaleGroup() {
            return innerTable;
        }
    }
}
