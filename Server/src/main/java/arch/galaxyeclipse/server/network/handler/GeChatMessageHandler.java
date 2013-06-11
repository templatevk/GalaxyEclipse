package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IGeServerNetworkManager;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeChatReceiveMessagePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

/**
 *
 */
class GeChatMessageHandler extends GePacketHandlerDecorator {
    private IGeServerNetworkManager networkManager;

    GeChatMessageHandler(IGeChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        networkManager = GeContextHolder.getBean(IGeServerNetworkManager.class);
    }

    @Override
    protected boolean handleImp(GePacket packet) {
        switch (packet.getType()) {
            case CHAT_SEND_MESSAGE:
                sendChatMessageBroadcast(packet.getChatSendMessage().getMessage(),
                        getServerChannelHandler().getPlayerInfoHolder().getPlayer().getUsername());
                return true;
        }
        return false;
    }

    private void sendChatMessageBroadcast(String message, String sender) {
        GeChatReceiveMessagePacket messagePacket = GeChatReceiveMessagePacket.newBuilder()
                .setMessage(message).setSender(sender).build();
        GePacket packet = GePacket.newBuilder()
                .setType(GePacket.Type.CHAT_RECEIVE_MESSAGE)
                .setChatReceiveMessage(messagePacket).build();
        networkManager.sendBroadcast(packet);
    }
}
