package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;
import lombok.Setter;

import static arch.galaxyeclipse.shared.GeConstants.LOCATION_TO_SCREEN_COORDS_COEF;

/**
 *
 */
@Getter
@Setter
public class LocationObjectActor extends ClickableActor {

    private LocationObjectPacket lop;
    private LocationObjectTypesMapperType locationObjectType;
    private boolean selectable = false;

    public LocationObjectActor(Drawable drawable, LocationObjectPacket locationObject) {
        this(drawable, locationObject, new StubCommand<GePosition>());
    }

    public LocationObjectActor(Drawable drawable, LocationObjectPacket lop,
            ICommand<GePosition> hitCommand) {

        super(drawable, null);

        boolean self = lop.getObjectId() ==
                getShipStateInfoHolder().getLocationObjectId();

        this.lop = lop;
        setHitCommand(hitCommand);
        setActorType(self ? ActorType.SELF : ActorType.LOCATION_OBJECT);

        // TODO: REMOVE!
        if (self) {
            getColor().r = 255;
        }
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
            float locationObjectX = lop.getPositionX();
            float locationObjectY = lop.getPositionY();
            float screenDiffX = (locationObjectX - locationShipX) * LOCATION_TO_SCREEN_COORDS_COEF;
            float screenDiffY = (locationObjectY - locationShipY) * LOCATION_TO_SCREEN_COORDS_COEF;
            screenDiffX *= stageInfo.getScaleX();
            screenDiffY *= stageInfo.getScaleY();
            float screenObjectX = screenCenterX + screenDiffX;
            float screenObjectY = screenCenterY + screenDiffY;
            setPosition(screenObjectX, screenObjectY);
        }

        setRotation(lop.getRotationAngle());
        setScale(stageInfo.getScaleX(), stageInfo.getScaleY());
    }

    @Override
    protected boolean isSelectable() {
        return selectable;
    }

    @Override
    protected int compareToImpl(IGeActor actor) {
        return ((LocationObjectActor)actor).getLop().getObjectId() - lop.getObjectId();
    }
}
