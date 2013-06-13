package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.resource.IGeAudioManager;
import arch.galaxyeclipse.client.ui.actor.GeActor;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeClientActionPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.GeClientActionPacket.ClientActionType;
import static arch.galaxyeclipse.shared.protocol.GeProtocol.GeClientActionPacket.ClientActionType.*;

@Slf4j
class GeClientActionListener extends InputListener {

    private final IGeAudioManager audioManager;
    private
    @Setter
    boolean enabled = true;

    private IGeClientNetworkManager networkManager;

    public GeClientActionListener() {
        networkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);
        audioManager = GeContextHolder.getBean(IGeAudioManager.class);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (enabled) {
            return checkKeys(event);
        }
        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        if (enabled) {
            return checkKeys(event);
        }
        return false;
    }

    protected boolean checkKeys(InputEvent event) {
        switch (event.getKeyCode()) {
            case Input.Keys.SHIFT_LEFT:
                sendPacket(ROCKET_SHOOT);
                return true;
            case Input.Keys.SPACE:
                if (GeActor.getSelectedActor() != null) {
                    sendPacket(ATTACK);
                    audioManager.playShoot();
                }
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
        if (GeClientActionListener.log.isDebugEnabled()) {
            GeClientActionListener.log.debug("Client action " + type);
        }

        GePacket clientActionPacket = GePacket.newBuilder()
                .setType(GePacket.Type.CLIENT_ACTION)
                .setClientAction(GeClientActionPacket.newBuilder()
                        .setType(type))
                .build();

        networkManager.sendPacket(clientActionPacket);
    }
}
