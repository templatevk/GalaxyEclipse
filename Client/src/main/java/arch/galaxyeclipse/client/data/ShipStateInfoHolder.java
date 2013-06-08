package arch.galaxyeclipse.client.data;

import lombok.Data;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;

/**
 *
 */
@Data
@Slf4j
public class ShipStateInfoHolder {
    @Delegate
    private ShipStateResponse ssResponse;

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
}
