package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Data
@Slf4j
public class ShipStateInfoHolder {
    private int moveSpeed;
    private float rotationSpeed;
    private int hp;
    private int armorDurability;
    private float rotationAngle;
    private float positionX;
    private float positionY;
    private int locationObjectId;

    public void setShipState(GeProtocol.ShipStateResponse shipStateResponse) {
        setArmorDurability(shipStateResponse.getArmorDurability());
        setHp(shipStateResponse.getHp());
        setMoveSpeed(shipStateResponse.getMoveSpeed());
        setPositionX(shipStateResponse.getPositionX());
        setPositionY(shipStateResponse.getPositionY());
        setRotationAngle(shipStateResponse.getRotationAngle());
        setRotationSpeed(shipStateResponse.getRotationSpeed());
        setLocationObjectId(shipStateResponse.getLocationObjectId());

        if (log.isDebugEnabled()) {
            log.debug("Updating ship state");
            log.debug("\tMove speed " + moveSpeed);
            log.debug("\tRotation speed " + rotationSpeed);
            log.debug("\tHp " + hp);
            log.debug("\tArmor durability " + armorDurability);
            log.debug("\tRotation angle " + rotationAngle);
            log.debug("\tGePosition x " + positionX);
            log.debug("\tGePosition y " + positionY);
        }
    }
}
