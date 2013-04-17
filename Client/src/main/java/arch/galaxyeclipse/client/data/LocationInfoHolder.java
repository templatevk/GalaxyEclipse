package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class LocationInfoHolder implements ILocationInfoHolder {
    private GeProtocol.LocationInfo locationInfo;

    LocationInfoHolder() {

    }

    @Override
    public void setLocationInfo(GeProtocol.LocationInfo locationInfo) {
        // TODO process location info
        if (LocationInfoHolder.log.isInfoEnabled()) {
            LocationInfoHolder.log.info("Updating location info");
        }
        this.locationInfo = locationInfo;

        // locationInfo.getLocationCachedObjects().getObjects(1).
    }
}
