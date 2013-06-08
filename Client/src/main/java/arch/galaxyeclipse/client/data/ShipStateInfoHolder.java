package arch.galaxyeclipse.client.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;

/**
 *
 */
@Data
@Slf4j
public class ShipStateInfoHolder {
    private float moveSpeed;
    private float rotationSpeed;
    private int hp;
    private int armorDurability;
    private float rotationAngle;
    private float positionX;
    private float positionY;
    private int locationObjectId;

    public void setShipState(ShipStateResponse ssResponse) {
        armorDurability = ssResponse.getArmorDurability();
        hp = ssResponse.getHp();
        moveSpeed = ssResponse.getMoveSpeed();
        positionX = ssResponse.getPositionX();
        positionY = ssResponse.getPositionY();
        rotationAngle = ssResponse.getRotationAngle();
        rotationSpeed = ssResponse.getRotationSpeed();
        locationObjectId = ssResponse.getLocationObjectId();

        if (log.isDebugEnabled()) {
            log.debug("Updating ship state");
            log.debug("\tMove speed " + moveSpeed);
            log.debug("\tRotation speed " + rotationSpeed);
            log.debug("\tHp " + hp);
            log.debug("\tArmor durability " + armorDurability);
            log.debug("\tRotation angle " + rotationAngle);
            log.debug("\tGePosition x " + positionX);
            log.debug("\tGePosition y " + positionY);
            log.debug("\tObject id " + locationObjectId);
        }
    }
}
