package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.context.*;
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
public class GeActor extends Image implements IGeActor {
    @Getter(AccessLevel.PROTECTED)
    private static ShipStateInfoHolder shipStateInfoHolder;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ActorType actorType;

    static {
        shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
    }

    public GeActor() {
        this(null);
        actorType = ActorType.UNDEFINED;
    }

    public GeActor(Drawable drawable) {
        super(drawable);
    }

    @Override
    public Actor toActor() {
        return this;
    }

    @Override
    public void adjust(StageInfo stageInfo) {

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hittedActor = super.hit(x, y, touchable);
        if (hittedActor == this) {
            if (GeActor.log.isDebugEnabled()) {
                GeActor.log.debug(LogUtils.getObjectInfo(GeActor.this) +
                        "hit (" + x + ", " + y + ")");
            }
        }
        return hittedActor;
    }

    @Override
    public int compareTo(IGeActor actor) {
        return actorType.compareTo(actor.getActorType());
    }
}
