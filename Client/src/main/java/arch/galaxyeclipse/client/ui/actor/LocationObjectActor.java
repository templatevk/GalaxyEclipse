package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Data;

import static arch.galaxyeclipse.shared.GeConstants.LOCATION_TO_SCREEN_COORDS_COEF;

/**
 *
 */
@Data
class LocationObjectActor extends ClickableActor {

    private LocationObjectPacket locationObject;
    private LocationObjectTypesMapperType locationObjectType;
    private boolean selectable = false;

    public LocationObjectActor(Drawable drawable, LocationObjectPacket locationObject) {
        this(drawable, locationObject, new StubCommand<GePosition>());
    }

    public LocationObjectActor(Drawable drawable, LocationObjectPacket locationObject,
            ICommand<GePosition> hitCommand) {

        super(drawable, null);

        boolean self = locationObject.getObjectId() ==
                getShipStateInfoHolder().getLocationObjectId();

        this.locationObject = locationObject;
        this.setHitCommand(hitCommand);
        this.setActorType(self ? ActorType.SELF : ActorType.PLAYER);

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
                selectable = true;
                break;
        }

        float screenCenterX = (stageInfo.getWidth() - getWidth()) / 2f;
        float screenCenterY = (stageInfo.getHeight() - getHeight()) / 2f;
        if (getActorType() == ActorType.SELF) {
            setPosition(screenCenterX, screenCenterY);
        } else {
            float locationShipX = getShipStateInfoHolder().getPositionX();
            float locationShipY = getShipStateInfoHolder().getPositionY();
            float locationObjectX = locationObject.getPositionX();
            float locationObjectY = locationObject.getPositionY();
            float screenDiffX = (locationObjectX - locationShipX) * LOCATION_TO_SCREEN_COORDS_COEF;
            float screenDiffY = (locationObjectY - locationShipY) * LOCATION_TO_SCREEN_COORDS_COEF;
            screenDiffX *= stageInfo.getScaleX();
            screenDiffY *= stageInfo.getScaleY();
            float screenObjectX = screenCenterX + screenDiffX;
            float screenObjectY = screenCenterY + screenDiffY;
            setPosition(screenObjectX, screenObjectY);
        }

        setRotation(locationObject.getRotationAngle());
        setScale(stageInfo.getScaleX(), stageInfo.getScaleY());
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }
}
