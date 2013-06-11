package arch.galaxyeclipse.client.ui.actor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class GeBackgroundActor extends GeClickableActor {

    public GeBackgroundActor(Drawable drawable) {
        super(drawable, GeActorType.BACKGROUND);
    }

    @Override
    public void adjust(GeStageInfo stageInfo) {
        setWidth(stageInfo.getWidth());
        setHeight(stageInfo.getHeight());
    }

    @Override
    public GeActorType getActorType() {
        return GeActorType.BACKGROUND;
    }

    @Override
    protected boolean isSelectable() {
        return false;
    }
}
