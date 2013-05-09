package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.util.ICommand;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class PacketProcessingListenerCommand implements IPacketProcessingListener {
    private GeProtocol.Packet.Type packetType;
    private ICommand<GeProtocol.Packet> command;

    public PacketProcessingListenerCommand(GeProtocol.Packet.Type packetType,
            ICommand<GeProtocol.Packet> command) {
        this.packetType = packetType;
        this.command = command;
    }

    @Override
    public void onProcessingComplete(GeProtocol.Packet packet) {
        command.perform(packet);
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(packetType);
    }
}
