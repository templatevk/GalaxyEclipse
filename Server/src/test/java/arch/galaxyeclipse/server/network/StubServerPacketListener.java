package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.protocol.*;

import java.util.*;

/**
 *
 */
public class StubServerPacketListener implements IServerPacketListener {
    public StubServerPacketListener() {

    }

    @Override
    public void onPacketReceived(GalaxyEclipseProtocol.Packet packet) {

    }

    @Override
    public List<GalaxyEclipseProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList();
    }
}
