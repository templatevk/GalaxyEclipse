package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IGeResourceLoader;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.network.IGeServerPacketListener;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class GeBottomPanelWidget extends Table implements IGeServerPacketListener {
    private final int DEFAULT_WIDTH = 1446;
    private final int DEFAULT_HEIGHT = 159;

    private IGeClientNetworkManager networkManager;
    private IGeResourceLoader resourceLoader;

    private Drawable background;

    public GeBottomPanelWidget() {
        networkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);

        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
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
    public List<GePacket.Type> getPacketTypes() {
        return Arrays.asList(GePacket.Type.CHAT_RECEIVE_MESSAGE);
        //TODO Change type
    }

    @Override
    public void onPacketReceived(GePacket packet) {
        switch (packet.getType()) {
            case CHAT_RECEIVE_MESSAGE:
                //TODO Change type
                break;
        }
    }
}
