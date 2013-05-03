package arch.galaxyeclipse.client.ui.actor;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class BackgroundActor extends ClickableActor {
    public BackgroundActor(Drawable drawable) {
        super(drawable);
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
