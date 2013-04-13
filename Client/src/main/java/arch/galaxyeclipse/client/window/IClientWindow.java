package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;

/**
 *
 */
public interface IClientWindow {
    void setStagePresenter(IStagePresenter stagePresenter);

    void setStage(AbstractGameStage gameStage);

    int getViewportHeight();

    int getViewportWidth() ;
}
