package arch.galaxyeclipse.shared.protocol;

import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket;
import arch.galaxyeclipse.shared.common.ICommand;

import java.io.Serializable;

/**
 * A kind of RPC to change GameInfo on client side.
 */
public interface ShipStaticInfoCommand extends Serializable, ICommand<ShipStaticInfoPacket> {

}