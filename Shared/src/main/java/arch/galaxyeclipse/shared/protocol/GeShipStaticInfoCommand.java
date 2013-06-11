package arch.galaxyeclipse.shared.protocol;

import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStaticInfoPacket;

import java.io.Serializable;

/**
 * A kind of RPC to change GameInfo on client side.
 */
public interface GeShipStaticInfoCommand extends Serializable, IGeCommand<GeShipStaticInfoPacket> {

}