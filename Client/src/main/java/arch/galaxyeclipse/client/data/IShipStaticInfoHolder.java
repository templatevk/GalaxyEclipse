package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.protocol.*;

/**
 *
 */
public interface IShipStaticInfoHolder extends IServerPacketListener {
    void setShipStaticInfo(GeProtocol.ShipStaticInfo shipStaticInfo);
}
