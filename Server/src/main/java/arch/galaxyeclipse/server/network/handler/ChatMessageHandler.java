package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IServerNetworkManager;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;

/**
 *
 */
class ChatMessageHandler extends PacketHandlerDecorator {
    private IServerNetworkManager networkManager;

    ChatMessageHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        networkManager = ContextHolder.getBean(IServerNetworkManager.class);
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CHAT_SEND_MESSAGE:
                sendChatMessageBroadcast(packet.getChatSendMessage().getMessage(),
                        getServerChannelHandler().getPlayerInfoHolder().getPlayer().getUsername());
                return true;
        }
        return false;
    }

    private void sendChatMessageBroadcast(String message, String sender) {
        GeProtocol.ChatReceiveMessagePacket messagePacket = GeProtocol.ChatReceiveMessagePacket.newBuilder()
                .setMessage(message).setSender(sender).build();
        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.CHAT_RECEIVE_MESSAGE)
                .setChatReceiveMessage(messagePacket).build();
        networkManager.sendBroadcast(packet);
    }
}
