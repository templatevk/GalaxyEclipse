package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;

/**
 *
 */
public interface IClientWindow {
    void setStagePresenter(IStagePresenter stagePresenter);

    int getViewportHeight();

    int getViewportWidth() ;
}
