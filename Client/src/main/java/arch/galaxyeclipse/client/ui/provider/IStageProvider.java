package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.AbstractGameStage;

/**
 *
 */
public interface IStageProvider {
    AbstractGameStage getGameStage();
    StageProviderType getType();
    void detach();
}
