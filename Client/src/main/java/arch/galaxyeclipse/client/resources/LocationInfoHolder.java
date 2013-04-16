package arch.galaxyeclipse.client.resources;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.protocol.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
class LocationInfoHolder extends ServerPacketListener implements ILocationInfoHolder {
    private GeProtocol.LocationInfo locationInfo;

    LocationInfoHolder() {

    }

    @Override
    public void setLocationInfo(GeProtocol.LocationInfo locationInfo) {
        // TODO process location info
        if (log.isInfoEnabled()) {
            log.info("Updating location info");
        }
        this.locationInfo = locationInfo;

        locationInfo.get
    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.Packet packet) {

        switch (packet.getType()) {
            case LOCATION_INFO:
                setLocationInfo(packet.getLocationInfo());
                break;
        }
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GeProtocol.Packet.Type.LOCATION_INFO);
    }
}
