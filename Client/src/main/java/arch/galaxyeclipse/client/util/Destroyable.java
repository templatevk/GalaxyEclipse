package arch.galaxyeclipse.client.util;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.util.*;

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
