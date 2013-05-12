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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
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
    private final int DEFAULT_MESSAGES_FIELD_HEIGHT = 360;
    private final int DEFAULT_TEXT_FIELD_PADDING_BOTTOM = 80;
    private final int DEFAULT_TEXT_FIELD_PADDING_LEFT = 10;
    private final int DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM = 150;
    private final int DEFAULT_MESSAGES_FIELD_PADDING_LEFT = 25;
    private final int TEXT_FIELD_TEXT_PADDING_X = 10;
    private final int TEXT_FIELD_TEXT_PADDING_Y = -10;
    private final int CHAT_BUTTON_SCROLL_UP_PADDING_LEFT = 160;
    private final int CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM = 28;
    private final int CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT = 72;
    private final int CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM = 28;
    private final int CHAT_BUTTON_AUTO_SCROLL_PADDING_LEFT = 10;
    private final int CHAT_BUTTON_AUTO_SCROLL_PADDING_BOTTOM = 28;

    private float fontHeight;

    IResourceLoader resourceLoader;

    private Drawable background;
    private TextField textField;
    private Table textFieldTable;
    private Label messagesField;
    private ScrollPane messagesScrollPane;
    private Label.LabelStyle labelStyle;
    TextButton scrollUpBtn;
    Table scrollUpBtnTable;
    TextButton scrollDownBtn;
    Table scrollDownBtnTable;
    TextButton autoScrollBtn;
    Table autoScrollBtnTable;
    private IClientNetworkManager networkManager;

    private boolean isAutoScrollEnabled;

    public ChatWidget() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        isAutoScrollEnabled = true;

        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat/chat"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Drawable textFieldBackground = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat/chatTextField"));
        Drawable carret = new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        Drawable selection = new TextureRegionDrawable(resourceLoader.findRegion("ui/selection"));
        BitmapFont font = resourceLoader.getFont("assets/font_calibri_36px");
        fontHeight = font.getLineHeight();
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

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat/chatVerticalScroll"));
        messagesScrollPane = new ScrollPane(messagesField,scrollPaneStyle);
        messagesScrollPane.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT, DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM,
                DEFAULT_MESSAGES_FIELD_WIDTH, DEFAULT_MESSAGES_FIELD_HEIGHT);
        addActor(messagesScrollPane);
        messagesScrollPane.addListener(new DragScrollListener(messagesScrollPane){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if(isAutoScrollEnabled)
                    disableAutoScroll();
                return super.scrolled(event, x, y, amount);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        messagesScrollPane.setMySinglePropertyListener(new IPropertyListener<Float>() {
            @Override
            public void onPropertyChanged(Float newValue) {
                if(isAutoScrollEnabled)
                    messagesScrollPane.setScrollY(newValue);
            }
        });

        scrollUpBtn = StageUiFactory.createButtonBuilder()
                .setType(IButtonBuilder.ButtonType.GameChatInnerScrollUpButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        messagesScrollPane.setScrollY(messagesScrollPane.getScrollY() - fontHeight);
                        if(isAutoScrollEnabled)
                            disableAutoScroll();
                    }
                }).build();

        scrollUpBtnTable = new Table();
        scrollUpBtnTable.setBounds(CHAT_BUTTON_SCROLL_UP_PADDING_LEFT, CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM,
                scrollUpBtn.getWidth(), scrollUpBtn.getHeight());
        scrollUpBtnTable.setTransform(true);
        scrollUpBtnTable.row();
        scrollUpBtnTable.add(scrollUpBtn);

        addActor(scrollUpBtnTable);

        scrollDownBtn = StageUiFactory.createButtonBuilder()
                .setType(IButtonBuilder.ButtonType.GameChatInnerScrollDownButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        messagesScrollPane.setScrollY(messagesScrollPane.getScrollY() + fontHeight);
                        if(isAutoScrollEnabled)
                            disableAutoScroll();
                    }
                }).build();

        scrollDownBtnTable = new Table();
        scrollDownBtnTable.setBounds(CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT, CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM,
                scrollDownBtn.getWidth(), scrollDownBtn.getHeight());
        scrollDownBtnTable.setTransform(true);
        scrollDownBtnTable.row();
        scrollDownBtnTable.add(scrollDownBtn);

        addActor(scrollDownBtnTable);

        autoScrollBtn = StageUiFactory.createButtonBuilder()
                .setType(IButtonBuilder.ButtonType.GameChatInnerAutoScrollButton)
                .setClickCommand(new IButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        if(!isAutoScrollEnabled){
                            enableAutoScroll();
                            messagesScrollPane.setScrollY(messagesScrollPane.getMaxY());
                        }
                    }
                }).build();

        autoScrollBtnTable = new Table();
        autoScrollBtnTable.setBounds(CHAT_BUTTON_AUTO_SCROLL_PADDING_LEFT, CHAT_BUTTON_AUTO_SCROLL_PADDING_BOTTOM,
                autoScrollBtn.getWidth(), autoScrollBtn.getHeight());
        autoScrollBtnTable.setTransform(true);
        autoScrollBtnTable.row();
        autoScrollBtnTable.add(autoScrollBtn);

        addActor(autoScrollBtnTable);

        networkManager.addPacketListener(this);
    }

    private void enableAutoScroll() {
        isAutoScrollEnabled = true;
        autoScrollBtn.getStyle().up = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat/btnChatAutoScroll"));
    }

    private void disableAutoScroll() {
        isAutoScrollEnabled = false;
        autoScrollBtn.getStyle().up = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat/btnChatAutoScrollDeactivated"));
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();
        textFieldTable.setScale(scaleX, scaleY);
        textFieldTable.setX(DEFAULT_TEXT_FIELD_PADDING_LEFT * scaleX);
        textFieldTable.setY(DEFAULT_TEXT_FIELD_PADDING_BOTTOM * scaleY);
        messagesScrollPane.setScale(scaleX, scaleY) ;
        messagesScrollPane.setX(DEFAULT_MESSAGES_FIELD_PADDING_LEFT * scaleX);
        messagesScrollPane.setY(DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM * scaleY);
        scrollUpBtnTable.setScale(scaleX, scaleY) ;
        scrollUpBtnTable.setX(CHAT_BUTTON_SCROLL_UP_PADDING_LEFT * scaleX);
        scrollUpBtnTable.setY(CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM * scaleY);
        scrollDownBtnTable.setScale(scaleX, scaleY) ;
        scrollDownBtnTable.setX(CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT * scaleX);
        scrollDownBtnTable.setY(CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM * scaleY);
        autoScrollBtnTable.setScale(scaleX, scaleY) ;
        autoScrollBtnTable.setX(CHAT_BUTTON_AUTO_SCROLL_PADDING_LEFT * scaleX);
        autoScrollBtnTable.setY(CHAT_BUTTON_AUTO_SCROLL_PADDING_BOTTOM * scaleY);
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
                GeProtocol.ChatReceiveMessagePacket messagePacket = packet.getChatReceiveMessage();
                messagesField.setText(messagesField.getText() +
                        messagePacket.getSender() + " : " + messagePacket.getMessage() + "\n");
//                if (isAutoScrollEnabled)
//                    messagesScrollPane.setScrollY(messagesScrollPane.getMaxY());
                break;
        }
    }
}
