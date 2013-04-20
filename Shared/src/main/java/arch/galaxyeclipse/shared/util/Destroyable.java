package arch.galaxyeclipse.shared.util;

import arch.galaxyeclipse.shared.context.*;

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
