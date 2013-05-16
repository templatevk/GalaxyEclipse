package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.IServerPacketListener;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class StateWidget extends Table implements IServerPacketListener {
    private final int DEFAULT_WIDTH = 398;
    private final int DEFAULT_HEIGHT = 345;
    private static final int HP_LABEL_PADDING_BOTTOM = 230;
    private static final int HP_LABEL_PADDING_LEFT = 90;

    private IClientNetworkManager networkManager;
    private IResourceLoader resourceLoader;

    private Drawable background;

    private Label hpLabel;
    private Table hpLabelTable;

    public StateWidget() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/state/state"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Label.LabelStyle hpLabelStyle = new Label.LabelStyle(
                resourceLoader.getFont("font_calibri_36px"), Color.RED);
        hpLabel = new Label("",hpLabelStyle);
        hpLabelTable = new Table();
        hpLabelTable.setTransform(true);
        hpLabelTable.row();
        hpLabelTable.add(hpLabel);
        addActor(hpLabelTable);

        networkManager.addPacketListener(this);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();

        hpLabelTable.setScale(scaleX,scaleY);
        hpLabelTable.setX(HP_LABEL_PADDING_LEFT * scaleX);
        hpLabelTable.setY(HP_LABEL_PADDING_BOTTOM * scaleY);

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
        return Arrays.asList(GeProtocol.Packet.Type.SHIP_STATE_RESPONSE);
    }

    @Override
    public void onPacketReceived(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case SHIP_STATE_RESPONSE:
                hpLabel.setText("[TEST] HP = " + packet.getShipStateResponse().getHp());
                break;
        }
    }
}
