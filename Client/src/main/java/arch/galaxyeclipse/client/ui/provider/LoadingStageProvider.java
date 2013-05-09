package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.AbstractGameStage;
import arch.galaxyeclipse.client.ui.view.LoadingStage;

/**
 *
 */
public class LoadingStageProvider implements IStageProvider {
    private LoadingStage stage;

    public LoadingStageProvider() {
        stage = new LoadingStage();
    }

    @Override
    public AbstractGameStage getGameStage() {
        return stage;
    }

    @Override
    public void detach() {

    }
}
