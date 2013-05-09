package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.IStageProvider;
import arch.galaxyeclipse.shared.util.IDestroyable;

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
