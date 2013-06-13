package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;
import lombok.Data;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Data
@Slf4j
public class GeShipStateInfoHolder {

    private @Delegate GeShipStateResponse ssResponse = GeShipStateResponse.getDefaultInstance();

    public void setShipState(GeShipStateResponse ssResponse) {
        this.ssResponse = ssResponse;

        if (GeShipStateInfoHolder.log.isDebugEnabled()) {
            GeShipStateInfoHolder.log.debug("Updating ship state");
            GeShipStateInfoHolder.log.debug("\tMove speed " + ssResponse.getMoveSpeed());
            GeShipStateInfoHolder.log.debug("\tRotation speed " + ssResponse.getRotationSpeed());
            GeShipStateInfoHolder.log.debug("\tHp " + ssResponse.getHp());
            GeShipStateInfoHolder.log.debug("\tEnergy " + ssResponse.getEnergy());
            GeShipStateInfoHolder.log.debug("\tArmor durability " + ssResponse.getArmorDurability());
            GeShipStateInfoHolder.log.debug("\tRotation angle " + ssResponse.getRotationAngle());
            GeShipStateInfoHolder.log.debug("\tGePosition x " + ssResponse.getPositionX());
            GeShipStateInfoHolder.log.debug("\tGePosition y " + ssResponse.getPositionY());
            GeShipStateInfoHolder.log.debug("\tObject id " + ssResponse.getLocationObjectId());
        }
    }
}
