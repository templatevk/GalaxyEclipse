package arch.galaxyeclipse.client.util;

import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.common.IGeDisposable;

import static arch.galaxyeclipse.shared.context.GeContextHolder.getBean;

/**
 *
 */
public abstract class GeDisposable implements IGeDisposable {
    public GeDisposable() {
        getBean(IGeClientWindow.class).addDestroyable(this);
    }

    public static void addDestroyable(IGeDisposable destroyable) {
        getBean(IGeClientWindow.class).addDestroyable(destroyable);
    }
}
