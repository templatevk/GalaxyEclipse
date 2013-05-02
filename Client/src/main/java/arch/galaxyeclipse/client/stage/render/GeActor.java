package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
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
}
