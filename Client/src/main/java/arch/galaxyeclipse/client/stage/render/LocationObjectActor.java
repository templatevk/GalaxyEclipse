package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import arch.galaxyeclipse.shared.types.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
@Data
class LocationObjectActor extends ClickableActor {
    private LocationObject locationObject;
    private LocationObjectTypesMapperType locationObjectType;

    public LocationObjectActor(Drawable drawable, LocationObject locationObject) {
        this(drawable, locationObject, new StubCommand<GePosition>());
    }

    public LocationObjectActor(Drawable drawable, LocationObject locationObject,
            ICommand<GePosition> hitCommand) {
        super(drawable);
        this.locationObject = locationObject;

        setHitCommand(hitCommand);

        boolean self = locationObject.getObjectId() ==
                getShipStateInfoHolder().getLocationObjectId();
        setActorType(self ? ActorType.SELF : ActorType.PLAYER);
    }

    @Override
    public void adjust(StageInfo stageInfo) {
        switch (locationObjectType) {
            case ROCKET:
                break;
            case ASTEROID:
                break;
            case STATION:
                break;
            case PLAYER:
                if (getActorType() == ActorType.SELF) {
                    float x = (stageInfo.getWidth() - getWidth()) / 2f;
                    float y = (stageInfo.getHeight() - getHeight()) / 2f;
                    setPosition(x, y);
                } else {
                }
                break;
        }
        setScale(stageInfo.getScaleX(), stageInfo.getScaleY());
    }
}
