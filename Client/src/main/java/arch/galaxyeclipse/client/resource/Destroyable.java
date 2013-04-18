package arch.galaxyeclipse.client.resource;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;

/**
 *
 */
public abstract class Destroyable implements IDestroyable {
    public Destroyable() {
        ContextHolder.INSTANCE.getBean(IClientWindow.class).addDestroyable(this);
    }

    public static void addDestroyable(IDestroyable destroyable) {
        ContextHolder.INSTANCE.getBean(IClientWindow.class).addDestroyable(destroyable);
    }
}
