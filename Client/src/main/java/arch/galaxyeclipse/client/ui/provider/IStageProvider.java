package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.GeAbstractGameStage;

/**
 *
 */
public interface IStageProvider {

    GeAbstractGameStage getGameStage();

    GeStageProviderType getType();

    void detach();
}
