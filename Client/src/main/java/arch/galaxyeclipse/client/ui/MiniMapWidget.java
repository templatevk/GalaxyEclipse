package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.IServerPacketListener;
import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class MiniMapWidget extends Table implements IServerPacketListener {
    private final int DEFAULT_WIDTH = 570;
    private final int DEFAULT_HEIGHT = 591;

    IResourceLoader resourceLoader;

    private Drawable background;

    private IClientNetworkManager networkManager;

    public MiniMapWidget() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/minimap/minimap"));
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
    public void draw(SpriteBatch batch, float parentAlpha) {
        validate();
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
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
