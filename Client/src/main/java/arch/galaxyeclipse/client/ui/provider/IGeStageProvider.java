package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;

/**
 *
 */
public interface IGeStageProvider {

    GeAbstractGameStage getGameStage();

    void detach();
}
