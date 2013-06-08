package arch.galaxyeclipse.client.ui.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class BackgroundActor extends ClickableActor {

    public BackgroundActor(Drawable drawable) {
        super(drawable, ActorType.BACKGROUND);
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

    @Override
    protected boolean isSelectable() {
        return false;
    }

    @Override
    protected int compareToImpl(IGeActor actor) {
        return 0;
    }
}
