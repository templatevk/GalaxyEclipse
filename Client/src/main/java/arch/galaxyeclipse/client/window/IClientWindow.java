package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.shared.common.IDestroyable;

/**
 *
 */
public interface IClientWindow {
    float DEFAULT_WIDTH = 2048;
    float DEFAULT_HEIGHT = 1496;

    void setStageProvider(IStageProvider stagePresenter);

    void addDestroyable(IDestroyable destroyable);

    float getHeight();
    float getWidth() ;
    float getViewportHeight();
    float getViewportWidth() ;
}
