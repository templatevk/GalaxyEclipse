package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.*;
import arch.galaxyeclipse.shared.util.*;

/**
 *
 */
public interface IClientWindow {
    float DEFAULT_WIDTH = 1024;
    float DEFAULT_HEIGHT = 748;

    void setStageProvider(IStageProvider stagePresenter);

    void addDestroyable(IDestroyable destroyable);

    float getHeight();
    float getWidth() ;
}
