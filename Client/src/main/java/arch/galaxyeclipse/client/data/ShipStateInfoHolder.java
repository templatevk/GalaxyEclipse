package arch.galaxyeclipse.client.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;

/**
 *
 */
@Data
@Slf4j
public class ShipStateInfoHolder {

    private ShipStateResponse ssResponse = ShipStateResponse.getDefaultInstance();

    public void setShipState(ShipStateResponse ssResponse) {
        this.ssResponse = ssResponse;

        if (log.isDebugEnabled()) {
            log.debug("Updating ship state");
            log.debug("\tMove speed " + ssResponse.getMoveSpeed());
            log.debug("\tRotation speed " + ssResponse.getRotationSpeed());
            log.debug("\tHp " + ssResponse.getHp());
            log.debug("\tArmor durability " + ssResponse.getArmorDurability());
            log.debug("\tRotation angle " + ssResponse.getRotationAngle());
            log.debug("\tGePosition x " + ssResponse.getPositionX());
            log.debug("\tGePosition y " + ssResponse.getPositionY());
            log.debug("\tObject id " + ssResponse.getLocationObjectId());
        }
    }

    public float getMoveSpeed() {
        return ssResponse.getMoveSpeed();
    }

    public float getRotationSpeed() {
        return ssResponse.getRotationSpeed();
    }

    public int getHp() {
        return ssResponse.getHp();
    }

    public int getArmorDurability() {
        return ssResponse.getArmorDurability();
    }

    public float getRotationAngle() {
        return ssResponse.getRotationAngle();
    }

    public float getPositionX() {
        return ssResponse.getPositionX();
    }

    public float getPositionY() {
        return ssResponse.getPositionY();
    }

    public int getLocationObjectId() {
        return ssResponse.getLocationObjectId();
    }
}
