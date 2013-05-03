package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.extern.slf4j.*;

@Slf4j
class ClientActionListener extends InputListener {
    private IClientNetworkManager networkManager;

    public ClientActionListener() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return checkKeys(event);
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        return checkKeys(event);
    }

    protected boolean checkKeys(InputEvent event) {
        switch (event.getKeyCode()) {
            case Input.Keys.SHIFT_LEFT:
                sendPacket(GeProtocol.ClientAction.ClientActionType.ROCKET_SHOOT);
                return true;
            case Input.Keys.SPACE:
                sendPacket(GeProtocol.ClientAction.ClientActionType.ATTACK);
                return true;
            case Input.Keys.W:
                sendPacket(GeProtocol.ClientAction.ClientActionType.MOVE);
                return true;
            case Input.Keys.A:
                sendPacket(GeProtocol.ClientAction.ClientActionType.ROTATE_LEFT);
                return true;
            case Input.Keys.S:
                sendPacket(GeProtocol.ClientAction.ClientActionType.STOP);
                return true;
            case Input.Keys.D:
                sendPacket(GeProtocol.ClientAction.ClientActionType.ROTATE_RIGHT);
                return true;
        }
        return false;
    }

    protected void sendPacket(GeProtocol.ClientAction.ClientActionType type) {
        if (log.isDebugEnabled()) {
            log.debug("Client action " + type);
        }

        GeProtocol.ClientAction action = GeProtocol.ClientAction.newBuilder()
                .setType(type).build();
        GeProtocol.Packet clientActionPacket = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.CLIENT_ACTION)
                .setClientAction(action).build();
        networkManager.sendPacket(clientActionPacket);
    }
}
