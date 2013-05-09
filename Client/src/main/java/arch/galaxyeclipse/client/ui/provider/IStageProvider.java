package arch.galaxyeclipse.client.ui.provider;

import arch.galaxyeclipse.client.ui.view.AbstractGameStage;

/**
 *
 */
public interface IStageProvider {
    AbstractGameStage getGameStage();

    void detach();
}
