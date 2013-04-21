package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.stage.*;
import arch.galaxyeclipse.shared.util.*;

/**
 *
 */
public interface IClientWindow {
    float DEFAULT_WIDTH = 1024;
    float DEFAULT_HEIGHT = 748;

    void setStageProvider(IStageProvider stagePresenter);

    void addDestroyable(IDestroyable destroyable);

    int getViewportHeight();
    int getViewportWidth() ;
    int getHeight();
    int getWidth() ;
}
