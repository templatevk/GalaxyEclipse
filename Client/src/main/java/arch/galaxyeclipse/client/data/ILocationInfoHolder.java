package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.protocol.*;

/**
 *
 */
public interface ILocationInfoHolder {
    void setLocationInfo(GeProtocol.LocationInfo locationInfo);


    // TODO some method to query for objects in radius, determine radius as constant in shared module
    // TODO try to cache location background region for all positions
}
