package arch.galaxyeclipse.client.util;

import arch.galaxyeclipse.client.window.IClientWindow;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.common.IDestroyable;

/**
 *
 */
public abstract class Destroyable implements IDestroyable {
    public Destroyable() {
        ContextHolder.getBean(IClientWindow.class).addDestroyable(this);
    }

    public static void addDestroyable(IDestroyable destroyable) {
        ContextHolder.getBean(IClientWindow.class).addDestroyable(destroyable);
    }
}
