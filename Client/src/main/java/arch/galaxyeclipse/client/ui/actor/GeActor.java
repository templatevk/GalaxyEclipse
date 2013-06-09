package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

    private @Getter StageInfo stageInfo;

    protected int compareToImpl(IGeActor actor) {
        return 1;
    }

    public static IGeActor newStub() {
        return new GeActor() {
            @Override
            protected int compareToImpl(IGeActor actor) {
                return 0;
            }
        };
    }

    public GeActor() {
        this(null, ActorType.UNDEFINED);
    }

    public GeActor(Drawable drawable, ActorType actorType) {
        super(drawable);
        this.actorType = actorType;
    }

    @Override
    public Actor toActor() {
        return this;
    }

    @Override
    public void adjust(StageInfo stageInfo) {
        this.stageInfo = stageInfo;
    }

    @Override
    public int compareTo(IGeActor actor) {
        int compareResult = actorType.compareTo(actor.getActorType());
        return compareResult == 0 ? compareToImpl(actor) : compareResult;
    }
}
