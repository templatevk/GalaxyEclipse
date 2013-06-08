package arch.galaxyeclipse.client.util;

import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.common.IDestroyable;

import static arch.galaxyeclipse.shared.context.ContextHolder.getBean;

/**
 *
 */
public abstract class Destroyable implements IDestroyable {
    public Destroyable() {
        getBean(IClientWindow.class).addDestroyable(this);
    }

    public static void addDestroyable(IDestroyable destroyable) {
        getBean(IClientWindow.class).addDestroyable(destroyable);
    }
}
