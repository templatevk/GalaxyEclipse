package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;
import arch.galaxyeclipse.client.ui.view.GeLoadingStage;

/**
 *
 */
public class GeLoadingStageProvider implements IGeStageProvider {

    private GeLoadingStage stage;

    public GeLoadingStageProvider() {
        stage = new GeLoadingStage();
    }

    @Override
    public GeAbstractGameStage getGameStage() {
        return stage;
    }

    @Override
    public void detach() {

    }
}
