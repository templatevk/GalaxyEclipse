package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class GePacketProcessingListenerCommand implements IGePacketProcessingListener {
    private GePacket.Type packetType;
    private IGeCommand<GePacket> command;

    public GePacketProcessingListenerCommand(GePacket.Type packetType,
            IGeCommand<GePacket> command) {
        this.packetType = packetType;
        this.command = command;
    }

    @Override
    public void onProcessingComplete(GePacket packet) {
        command.perform(packet);
    }

    @Override
    public List<GePacket.Type> getPacketTypes() {
        return Arrays.asList(packetType);
    }
}
