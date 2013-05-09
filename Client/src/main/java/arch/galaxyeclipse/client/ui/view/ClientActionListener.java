package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.extern.slf4j.Slf4j;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket.ClientActionType;
import static arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket.ClientActionType.*;

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
                sendPacket(ROCKET_SHOOT);
                return true;
            case Input.Keys.SPACE:
                sendPacket(ATTACK);
                return true;
            case Input.Keys.W:
                sendPacket(event.getType() == Type.keyUp ? MOVE_UP : MOVE_DOWN);
                return true;
            case Input.Keys.S:
                sendPacket(event.getType() == Type.keyUp ? STOP_UP : STOP_DOWN);
                return true;
            case Input.Keys.A:
                sendPacket(event.getType() == Type.keyUp ? ROTATE_LEFT_UP : ROTATE_LEFT_DOWN);
                return true;
            case Input.Keys.D:
                sendPacket(event.getType() == Type.keyUp ? ROTATE_RIGHT_UP : ROTATE_RIGHT_DOWN);
                return true;
        }
        return false;
    }

    protected void sendPacket(ClientActionType type) {
        if (log.isDebugEnabled()) {
            log.debug("Client action " + type);
        }

        ClientActionPacket action = ClientActionPacket.newBuilder()
                .setType(type).build();
        GeProtocol.Packet clientActionPacket = GeProtocol.Packet.newBuilder()
                .setType(GeProtocol.Packet.Type.CLIENT_ACTION)
                .setClientAction(action).build();
        networkManager.sendPacket(clientActionPacket);
    }
}
