package arch.galaxyeclipse.shared.protocol;

import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStaticInfoPacket;
import arch.galaxyeclipse.shared.util.*;

import java.io.*;

/**
 * A kind of RPC to change GameInfo on client side.
 */
public interface ShipStaticInfoCommand extends Serializable, ICommand<ShipStaticInfoPacket> {

}