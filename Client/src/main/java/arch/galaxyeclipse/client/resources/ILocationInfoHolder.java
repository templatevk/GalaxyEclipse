package arch.galaxyeclipse.client.resources;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.protocol.*;

/**
 *
 */
public interface ILocationInfoHolder extends IServerPacketListener {
    void setLocationInfo(GeProtocol.LocationInfo locationInfo);

    // TODO some method to query for objects in radius, determine radius as constant in shared module
}
