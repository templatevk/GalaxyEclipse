package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.GeStubCommand;
import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
public class GeLocationObjectActor extends GeClickableActor {

    private GeLocationObjectPacket lop;
    private GeLocationObjectTypesMapperType locationObjectType;
    private boolean selectable = false;

    public GeLocationObjectActor(Drawable drawable, GeLocationObjectPacket locationObject) {
        this(drawable, locationObject, new GeStubCommand<GePosition>());
    }

    public GeLocationObjectActor(Drawable drawable, GeLocationObjectPacket lop,
            IGeCommand<GePosition> hitCommand) {

        super(drawable, null);

        boolean self = lop.getObjectId() ==
                getShipStateInfoHolder().getLocationObjectId();

        this.lop = lop;
        setHitCommand(hitCommand);
        setActorType(self ? GeActorType.SELF : GeActorType.LOCATION_OBJECT);
    }

    @Override
    public void adjust(GeStageInfo stageInfo) {
        super.adjust(stageInfo);
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
        if (getActorType() == GeActorType.SELF) {
            setPosition(screenCenterX, screenCenterY);
        } else {
            float locationShipX = getShipStateInfoHolder().getPositionX();
            float locationShipY = getShipStateInfoHolder().getPositionY();
            float locationObjectX = lop.getPositionX();
            float locationObjectY = lop.getPositionY();
            float screenDiffX = locationObjectX - locationShipX;
            float screenDiffY = locationObjectY - locationShipY;
            screenDiffX *= stageInfo.getScaleX();
            screenDiffY *= stageInfo.getScaleY();
            float screenObjectX = screenCenterX + screenDiffX;
            float screenObjectY = screenCenterY + screenDiffY;
            setPosition(screenObjectX, screenObjectY);
        }

        setRotation(lop.getRotationAngle());
        if (!isSelected()) {
            setScale(stageInfo.getScaleX(), stageInfo.getScaleY());
        }
    }

    @Override
    protected boolean isSelectable() {
        return selectable;
    }

    @Override
    protected int compareToImpl(GeActor actor) {
        return ((GeLocationObjectActor)actor).getLop().getObjectId() - lop.getObjectId();
    }
}
