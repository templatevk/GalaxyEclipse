package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;
import arch.galaxyeclipse.shared.util.*;

/**
 *
 */
public interface IClientWindow {
    float DEFAULT_WIDTH = 1024;
    float DEFAULT_HEIGHT = 748;

    void setStagePresenter(IStagePresenter stagePresenter);

    int getViewportHeight();

    int getViewportWidth() ;

    void addDestroyable(IDestroyable destroyable);
}
