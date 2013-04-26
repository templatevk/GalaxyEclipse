package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class BackgroundActor extends ClickableActor {

    // TODO because of delegation npe is thrown getPrefWidth()7m92ba7m92ba7m92ba
    @Delegate
    private Table background;

    public BackgroundActor() {
        this(null);
    }

    public BackgroundActor(Drawable drawable) {
        super(drawable);
        background = new Table();
        background.setBackground(drawable);

        setHitCommand(new ICommand<GePosition>() {
            @Override
            public void perform(GePosition argument) {
                if (log.isDebugEnabled()) {
                    log.debug("Background hit (" + argument.getX()
                            + ", " + argument.getY() + ")");
                }
            }
        });
        setActorType(ActorType.BACKGROUND);

        if (EnvType.CURRENT == EnvType.DEV) {
            background.debug();
        }
    }
}
