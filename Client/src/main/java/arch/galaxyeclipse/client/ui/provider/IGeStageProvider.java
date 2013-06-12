package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;

/**
 *
 */
public interface IGeStageProvider {

    GeStageProviderType getType();

    GeAbstractGameStage getGameStage();

    void detach();
}
