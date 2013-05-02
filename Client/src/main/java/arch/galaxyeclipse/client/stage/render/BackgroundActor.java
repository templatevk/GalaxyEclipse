package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class BackgroundActor extends ClickableActor {
    public BackgroundActor(Drawable drawable) {
        super(drawable);
        this.setHitCommand(new ICommand<GePosition>() {
            @Override
            public void perform(GePosition argument) {
                if (BackgroundActor.log.isDebugEnabled()) {
                    BackgroundActor.log.debug("Background hit (" +
                            argument.getY() + ", " +
                            argument.getY() + ")");
                }
            }
        });
    }

    @Override
    public void adjust(StageInfo stageInfo) {
        setWidth(stageInfo.getWidth());
        setHeight(stageInfo.getHeight());
    }

    @Override
    public ActorType getActorType() {
        return ActorType.BACKGROUND;
    }

    @Override
    public Actor toActor() {
        return this;
    }
}
