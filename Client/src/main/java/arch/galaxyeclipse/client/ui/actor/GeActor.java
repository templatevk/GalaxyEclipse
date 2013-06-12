package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.data.GeShipStateInfoHolder;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.shared.context.GeContextHolder;
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
public abstract class GeActor extends Image implements Comparable<GeActor> {

    @Getter(AccessLevel.PROTECTED)
    private static GeShipStateInfoHolder shipStateInfoHolder;
    @Getter(AccessLevel.PROTECTED)
    private static IGeClientNetworkManager clientNetworkManager;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private GeActorType actorType;

    static {
        shipStateInfoHolder = GeContextHolder.getBean(GeShipStateInfoHolder.class);
        clientNetworkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);
    }

    private @Getter GeStageInfo stageInfo;

    protected int compareToImpl(GeActor actor) {
        return 1;
    }

    public static GeActor newStub() {
        return new GeActor() {
            @Override
            protected int compareToImpl(GeActor actor) {
                return 0;
            }
        };
    }

    public static GeActor getSelectedActor() {
        return GeClickableActor.selectedActor;
    }

    public GeActor() {
        this(null, GeActorType.UNDEFINED);
    }

    public GeActor(Drawable drawable, GeActorType actorType) {
        super(drawable);
        this.actorType = actorType;
    }

    public void adjust(GeStageInfo stageInfo) {
        this.stageInfo = stageInfo;
    }

    @Override
    public int compareTo(GeActor actor) {
        int compareResult = actorType.compareTo(actor.getActorType());
        return compareResult == 0 ? compareToImpl(actor) : compareResult;
    }
}
