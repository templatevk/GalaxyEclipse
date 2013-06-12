package arch.galaxyeclipse.client.window;

import arch.galaxyeclipse.client.ui.provider.IGeStageProvider;
import arch.galaxyeclipse.shared.common.IGeDisposable;

/**
 *
 */
public interface IGeClientWindow {

    int RENDER_REQUEST_MILLISECONDS_DELAY = 50;

    float DEFAULT_WIDTH = 1024;
    float DEFAULT_HEIGHT = 748;

    void setStageProvider(IGeStageProvider stagePresenter);

    void addDestroyable(IGeDisposable destroyable);

    float getHeight();

    float getWidth();

    float getViewportHeight();

    float getViewportWidth();

    float getStateTime();
}
