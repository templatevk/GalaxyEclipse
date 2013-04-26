package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
public class BackgroundActor extends Table {
    @Delegate
    private ClickableActor clickableActor;

    public BackgroundActor() {
        this(null);
    }

    public BackgroundActor(Drawable drawable) {
        setBackground(drawable);
        clickableActor = new ClickableActor();
        clickableActor.setHitCommand(new ICommand<GePosition>() {
            @Override
            public void perform(GePosition argument) {
                if (log.isDebugEnabled()) {
                    log.debug("Background hit (" + argument.getX()
                            + ", " + argument.getY() + ")");
                }
            }
        });
        clickableActor.setActorType(ActorType.BACKGROUND);

        if (EnvType.CURRENT == EnvType.DEV) {
            debug();
        }
    }
}
