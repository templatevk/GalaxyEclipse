package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.IServerPacketListener;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class BottomPanelWidget extends Table implements IServerPacketListener {
    private final int DEFAULT_WIDTH = 1446;
    private final int DEFAULT_HEIGHT = 159;

    private IClientNetworkManager networkManager;
    private IResourceLoader resourceLoader;

    private Drawable background;

    public BottomPanelWidget() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        setBackground(resourceLoader.createDrawable("ui/bottompanel/bottompanel"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        //logic

        networkManager.addPacketListener(this);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();
        //scale components
        super.setSize(width, height);
    }

    @Override
    public float getPrefWidth() {
        return DEFAULT_WIDTH;
    }

    @Override
    public float getPrefHeight() {
        return DEFAULT_HEIGHT;
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(GeProtocol.Packet.Type.CHAT_RECEIVE_MESSAGE);
        //TODO Change type
    }

    @Override
    public void onPacketReceived(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CHAT_RECEIVE_MESSAGE:
                //TODO Change type
                break;
        }
    }
}
