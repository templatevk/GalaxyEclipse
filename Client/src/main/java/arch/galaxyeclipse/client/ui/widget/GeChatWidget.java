package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IGeResourceLoader;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.client.network.IGeServerPacketListener;
import arch.galaxyeclipse.client.ui.GeStageUiFactory;
import arch.galaxyeclipse.client.ui.IGeButtonBuilder;
import arch.galaxyeclipse.client.ui.IGeButtonClickCommand;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeChatReceiveMessagePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeChatSendMessagePacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class GeChatWidget extends Table implements IGeServerPacketListener {

    private static final int DEFAULT_WIDTH = 570;
    private static final int DEFAULT_HEIGHT = 591;
    private static final int DEFAULT_MESSAGES_FIELD_WIDTH = 500;
    private static final int DEFAULT_MESSAGES_FIELD_HEIGHT = 350;
    private static final int DEFAULT_TEXT_FIELD_PADDING_BOTTOM = 80;
    private static final int DEFAULT_TEXT_FIELD_PADDING_LEFT = 10;
    private static final int DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM = 150;
    private static final int DEFAULT_MESSAGES_FIELD_PADDING_LEFT = 25;
    private static final int TEXT_FIELD_TEXT_PADDING_X = 10;
    private static final int TEXT_FIELD_TEXT_PADDING_Y = -10;
    private static final int CHAT_BUTTON_SCROLL_UP_PADDING_LEFT = 160;
    private static final int CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM = 28;
    private static final int CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT = 72;
    private static final int CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM = 28;
    private static final int CHAT_BUTTON_AUTO_SCROLL_PADDING_LEFT = 10;
    private static final int CHAT_BUTTON_AUTO_SCROLL_PADDING_BOTTOM = 28;

    private @Getter TextField textField;

    private IGeResourceLoader resourceLoader;
    private IGeClientNetworkManager networkManager;

    private Table textFieldTable;
    private Label messagesField;
    private ScrollPane messagesScrollPane;
    private TextButton scrollUpBtn;
    private Table scrollUpBtnTable;
    private TextButton scrollDownBtn;
    private Table scrollDownBtnTable;
    private TextButton autoScrollBtn;
    private Table autoScrollBtnTable;
    private boolean isAutoScrollEnabled;
    private float fontHeight;

    public GeChatWidget() {
        isAutoScrollEnabled = true;
        networkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        setBackground(resourceLoader.createDrawable("ui/chat/chat"));

        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Drawable textFieldBackground = resourceLoader.createDrawable("ui/chat/chatTextField");
        Drawable carret = resourceLoader.createDrawable("ui/carret");
        Drawable selection = resourceLoader.createDrawable("ui/selection");
        BitmapFont font = resourceLoader.getFont("font_calibri_36px");
        fontHeight = font.getLineHeight();

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(
                font, Color.WHITE, carret, selection, textFieldBackground);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = resourceLoader.createDrawable("ui/chat/chatVerticalScroll");

        textField = new TextField("", textFieldStyle);
        textField.setWidth(textFieldBackground.getMinWidth());
        textField.setHeight(textFieldBackground.getMinHeight());
        textField.setCustomPrefWidth(textFieldBackground.getMinWidth());
        textField.setPrefHeight(textFieldBackground.getMinHeight());
        textField.setTextPaddingX(TEXT_FIELD_TEXT_PADDING_X);
        textField.setTextPaddingY(TEXT_FIELD_TEXT_PADDING_Y);
        textField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                switch (event.getKeyCode()) {
                    case Keys.ENTER:
                        GeChatSendMessagePacket messagePacket = GeChatSendMessagePacket.newBuilder()
                                .setMessage(textField.getText()).build();
                        GePacket packet = GePacket.newBuilder()
                                .setType(GePacket.Type.CHAT_SEND_MESSAGE)
                                .setChatSendMessage(messagePacket).build();
                        networkManager.sendPacket(packet);
                        textField.setText("");
                        break;
                    case Keys.ESCAPE:
                        getStage().setKeyboardFocus(null);
                }
                return super.keyTyped(event, character);
            }
        });

        textFieldTable = new Table();
        textFieldTable.setBounds(DEFAULT_TEXT_FIELD_PADDING_LEFT,
                DEFAULT_TEXT_FIELD_PADDING_BOTTOM,
                textField.getWidth(), textField.getHeight());
        textFieldTable.setTransform(true);
        textFieldTable.row();
        textFieldTable.add(textField);
        addActor(textFieldTable);

        messagesField = new Label("", labelStyle);
        messagesField.setWrap(true);
        messagesField.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT,
                DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM,
                DEFAULT_MESSAGES_FIELD_WIDTH,
                DEFAULT_MESSAGES_FIELD_HEIGHT);
        messagesField.setAlignment(-1, -1);

        messagesScrollPane = new ScrollPane(messagesField, scrollPaneStyle);
        messagesScrollPane.setBounds(DEFAULT_MESSAGES_FIELD_PADDING_LEFT,
                DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM,
                DEFAULT_MESSAGES_FIELD_WIDTH,
                DEFAULT_MESSAGES_FIELD_HEIGHT);
        messagesScrollPane.addListener(new DragScrollListener(messagesScrollPane) {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (isAutoScrollEnabled) {
                    disableAutoScroll();
                }
                return super.scrolled(event, x, y, amount);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });
        messagesScrollPane.setPropertyListener(new IPropertyListener<Float>() {
            @Override
            public void onPropertyChanged(Float newValue) {
                if (isAutoScrollEnabled) {
                    messagesScrollPane.setScrollY(newValue);
                }
            }
        });
        addActor(messagesScrollPane);

        scrollUpBtn = GeStageUiFactory.createButtonBuilder()
                .setType(IGeButtonBuilder.ButtonType.GAME_CHAT_INNER_SCROLL_UP_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        float scrollY = messagesScrollPane.getScrollY() - fontHeight;
                        messagesScrollPane.setScrollY(scrollY);
                        if (isAutoScrollEnabled) {
                            disableAutoScroll();
                        }
                    }
                }).build();

        scrollUpBtnTable = new Table();
        scrollUpBtnTable.setBounds(CHAT_BUTTON_SCROLL_UP_PADDING_LEFT,
                CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM,
                scrollUpBtn.getWidth(), scrollUpBtn.getHeight());
        scrollUpBtnTable.setTransform(true);
        scrollUpBtnTable.row();
        scrollUpBtnTable.add(scrollUpBtn);
        addActor(scrollUpBtnTable);

        scrollDownBtn = GeStageUiFactory.createButtonBuilder()
                .setType(IGeButtonBuilder.ButtonType.GAME_CHAT_INNER_SCROLL_DOWN_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        float scrollY = messagesScrollPane.getScrollY() + fontHeight;
                        messagesScrollPane.setScrollY(scrollY);
                        if (isAutoScrollEnabled) {
                            disableAutoScroll();
                        }
                    }
                }).build();

        scrollDownBtnTable = new Table();
        scrollDownBtnTable.setBounds(CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT,
                CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM,
                scrollDownBtn.getWidth(), scrollDownBtn.getHeight());
        scrollDownBtnTable.setTransform(true);
        scrollDownBtnTable.row();
        scrollDownBtnTable.add(scrollDownBtn);
        addActor(scrollDownBtnTable);

        autoScrollBtn = GeStageUiFactory.createButtonBuilder()
                .setType(IGeButtonBuilder.ButtonType.GAME_CHAT_INNER_AUTO_SCROLL_BUTTON)
                .setClickCommand(new IGeButtonClickCommand() {
                    @Override
                    public void execute(InputEvent e, float x, float y) {
                        if (!isAutoScrollEnabled) {
                            enableAutoScroll();
                            messagesScrollPane.setScrollY(messagesScrollPane.getMaxY());
                        }
                    }
                }).build();

        autoScrollBtnTable = new Table();
        autoScrollBtnTable.setBounds(CHAT_BUTTON_AUTO_SCROLL_PADDING_LEFT,
                CHAT_BUTTON_AUTO_SCROLL_PADDING_BOTTOM,
                autoScrollBtn.getWidth(), autoScrollBtn.getHeight());
        autoScrollBtnTable.setTransform(true);
        autoScrollBtnTable.row();
        autoScrollBtnTable.add(autoScrollBtn);
        addActor(autoScrollBtnTable);

        networkManager.addPacketListener(this);
    }

    private void enableAutoScroll() {
        isAutoScrollEnabled = true;
        autoScrollBtn.getStyle().up = resourceLoader.createDrawable(
                "ui/chat/btnChatAutoScroll");
    }

    private void disableAutoScroll() {
        isAutoScrollEnabled = false;
        autoScrollBtn.getStyle().up = resourceLoader.createDrawable(
                "ui/chat/btnChatAutoScrollDeactivated");
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();

        textFieldTable.setScale(scaleX, scaleY);
        textFieldTable.setX(DEFAULT_TEXT_FIELD_PADDING_LEFT * scaleX);
        textFieldTable.setY(DEFAULT_TEXT_FIELD_PADDING_BOTTOM * scaleY);

        messagesScrollPane.setScale(scaleX, scaleY);
        messagesScrollPane.setX(DEFAULT_MESSAGES_FIELD_PADDING_LEFT * scaleX);
        messagesScrollPane.setY(DEFAULT_MESSAGES_FIELD_PADDING_BOTTOM * scaleY);

        scrollUpBtnTable.setScale(scaleX, scaleY);
        scrollUpBtnTable.setX(CHAT_BUTTON_SCROLL_UP_PADDING_LEFT * scaleX);
        scrollUpBtnTable.setY(CHAT_BUTTON_SCROLL_UP_PADDING_BOTTOM * scaleY);

        scrollDownBtnTable.setScale(scaleX, scaleY);
        scrollDownBtnTable.setX(CHAT_BUTTON_SCROLL_DOWN_PADDING_LEFT * scaleX);
        scrollDownBtnTable.setY(CHAT_BUTTON_SCROLL_DOWN_PADDING_BOTTOM * scaleY);

        autoScrollBtnTable.setScale(scaleX, scaleY);
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
    public List<GePacket.Type> getPacketTypes() {
        return Arrays.asList(GePacket.Type.CHAT_RECEIVE_MESSAGE);
    }

    @Override
    public void onPacketReceived(GePacket packet) {
        switch (packet.getType()) {
            case CHAT_RECEIVE_MESSAGE:
                GeChatReceiveMessagePacket messagePacket = packet.getChatReceiveMessage();
                messagesField.setText(messagesField.getText() +
                        messagePacket.getSender() + " : " + messagePacket.getMessage() + "\n");
                break;
        }
    }
}
