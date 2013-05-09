package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.IServerPacketListener;
import arch.galaxyeclipse.client.resource.IResourceLoader;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ChatWidget extends Table implements IServerPacketListener {
    private final int DEFAULT_WIDTH = 570;
    private final int DEFAULT_HEIGHT = 591;
    private final int DEFAULT_MESSAGES_FIELD_WIDTH = 520;
    private final int DEFAULT_MESSAGES_FIELD_HEIGHT = 370;
    private final int DEFAULT_TEXT_FIELD_PADDING_BOTTOM = 80;
    private final int DEFAULT_TEXT_FIELD_PADDING_LEFT = 10;
    private final int DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM = 150;
    private final int DEFAULT_MESSAGES_FIELD_PADDING_LEFT = 25;
    private final int TEXT_FIELD_TEXT_PADDING_X = 10;
    private final int TEXT_FIELD_TEXT_PADDING_Y = -10;

    private Drawable background;
    private TextField textField;
    private Table textFieldTable;
    private Label messagesField;
    private ScrollPane messagesScrollPane;

    private Table scrollTable;
    private Label.LabelStyle labelStyle;

    private IClientNetworkManager networkManager;

    public ChatWidget() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Drawable textFieldBackground = new TextureRegionDrawable(resourceLoader.findRegion("ui/chatTextField"));
        Drawable carret = new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        Drawable selection = new TextureRegionDrawable(resourceLoader.findRegion("ui/selection"));
        BitmapFont font = resourceLoader.getFont("assets/font_calibri_36px");
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, carret, selection, textFieldBackground);
        textField = new TextField("", textFieldStyle);
        textField.setWidth(textFieldBackground.getMinWidth());
        textField.setHeight(textFieldBackground.getMinHeight());
        textField.setCustomPrefWidth(textFieldBackground.getMinWidth());
        textField.setPrefHeight(textFieldBackground.getMinHeight());
        textField.setTextPaddingX(TEXT_FIELD_TEXT_PADDING_X);
        textField.setTextPaddingY(TEXT_FIELD_TEXT_PADDING_Y);

        textFieldTable = new Table();

        textFieldTable.setBounds(DEFAULT_TEXT_FIELD_PADDING_LEFT, DEFAULT_TEXT_FIELD_PADDING_BOTTOM,
                textField.getWidth(), textField.getHeight());
        textFieldTable.setTransform(true);
        addActor(textFieldTable);

        textFieldTable.row();
        textFieldTable.add(textField);

        textField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                switch (event.getKeyCode()) {
                    case Keys.ENTER:
                        GeProtocol.ChatSendMessagePacket messagePacket = GeProtocol.ChatSendMessagePacket.newBuilder()
                                .setMessage(textField.getText()).build();
                        GeProtocol.Packet packet = GeProtocol.Packet.newBuilder()
                                .setType(GeProtocol.Packet.Type.CHAT_SEND_MESSAGE)
                                .setChatSendMessage(messagePacket).build();
                        networkManager.sendPacket(packet);
                        textField.setText("");
                        break;
                    case Keys.ESCAPE:
                        getStage().unfocus(textField);
                }
                return super.keyTyped(event, character);
            }
        });

        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        messagesField = new Label("", labelStyle);
        messagesField.setWrap(true);
        messagesField.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT, DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM,
                DEFAULT_MESSAGES_FIELD_WIDTH, DEFAULT_MESSAGES_FIELD_HEIGHT);
        messagesField.setAlignment(-1, -1);
        //addActor(messagesField);

        scrollTable = new Table();

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll= new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        scrollPaneStyle.vScrollKnob= new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        messagesScrollPane = new ScrollPane(messagesField,scrollPaneStyle);
        messagesScrollPane.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT, DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM,
                DEFAULT_MESSAGES_FIELD_WIDTH, DEFAULT_MESSAGES_FIELD_HEIGHT);

        messagesScrollPane.setScrollingDisabled(true,false);
        messagesScrollPane.setSmoothScrolling(true);
        messagesScrollPane.setScrollbarsOnTop(true);
        addActor(messagesScrollPane);

        networkManager.addPacketListener(this);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();
        textFieldTable.setScale(scaleX, scaleY);
        textFieldTable.setX(DEFAULT_TEXT_FIELD_PADDING_LEFT * scaleX);
        textFieldTable.setY(DEFAULT_TEXT_FIELD_PADDING_BOTTOM * scaleY);
        messagesField.setFontScale(scaleX, scaleY);
        messagesField.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT * scaleX,
                DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM * scaleY,
                DEFAULT_MESSAGES_FIELD_WIDTH,
                DEFAULT_MESSAGES_FIELD_HEIGHT * scaleY);
        messagesScrollPane.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT * scaleX,
                DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM * scaleY,
                DEFAULT_MESSAGES_FIELD_WIDTH,
                DEFAULT_MESSAGES_FIELD_HEIGHT * scaleY);
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
    }

    @Override
    public void onPacketReceived(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CHAT_RECEIVE_MESSAGE:
                messagesScrollPane.setScrollY(messagesScrollPane.getMaxY());
                messagesScrollPane.setScrollX(messagesScrollPane.getMaxX());
                GeProtocol.ChatReceiveMessagePacket messagePacket = packet.getChatReceiveMessage();
                messagesField.setText(messagesField.getText() +
                        messagePacket.getSender() + " : " + messagePacket.getMessage() + "\n");
                log.debug("--- messagesScrollPane.getScrollY() = "+messagesScrollPane.getScrollY());
                log.debug("--- messagesScrollPane.getMaxY()  = " + messagesScrollPane.getMaxY());
                log.debug("--- messagesScrollPane.getVisualScrollY()  = " +messagesScrollPane.getVisualScrollY());
                break;
        }
    }
}
