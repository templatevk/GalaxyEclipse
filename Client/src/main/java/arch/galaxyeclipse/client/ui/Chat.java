package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.resource.IResourceLoader;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Timer.Task;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.StringBuilder;
@Slf4j
public class Chat extends Table {

    private final int DEFAULT_WIDTH = 570;
    private final int DEFAULT_HEIGHT = 591;
    private final int DEFAULT_TEXT_FIELD_PADDING_BOTTOM = 80;
    private final int DEFAULT_TEXT_FIELD_PADDING_LEFT = 10;
    private final int TEXT_FIELD_TEXT_PADDING_X = 10;
    private final int TEXT_FIELD_TEXT_PADDING_Y = -10;

    private Drawable background;
    private TextField textField;

    private InputListener inputListener;
    private Table textFieldTable;
    private Label label;

    private IClientNetworkManager networkManager;

    public Chat() {
        networkManager = ContextHolder.getBean(IClientNetworkManager.class);

        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/chat"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Drawable textFieldBackground = new TextureRegionDrawable(resourceLoader.findRegion("ui/chatTextField"));
        Drawable carret = new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        Drawable selection = new TextureRegionDrawable(resourceLoader.findRegion("ui/selection"));
        BitmapFont font = resourceLoader.getFont("assets/font_calibri_36px");
        TextField.TextFieldStyle textFieldStyle= new TextField.TextFieldStyle(font,Color.WHITE,carret,selection,textFieldBackground);
        textField = new TextField("",textFieldStyle);
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

        textField.addListener(new InputListener(){
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                switch (event.getKeyCode()){
                    case Keys.ENTER:
                        label.setText(textField.getText());
                        GeProtocol.ChatSendMessage message = GeProtocol.ChatSendMessage.newBuilder()
                                .setMessage(textField.getText()).build();
                        GeProtocol.Packet messagePacket = GeProtocol.Packet.newBuilder()
                                .setType(GeProtocol.Packet.Type.CHAT_SEND_MESSAGE)
                                .setChatSendMessage(message).build();
                        networkManager.sendPacket(messagePacket);
                        textField.setText("");
                        break;
                    case Keys.ESCAPE:
                        getStage().unfocus(textField);
                }
                return super.keyTyped(event, character);
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.RED);
        label = new Label("TEST_LABEL",labelStyle);
        addActor(label);
        label.setY(200);
        label.setX(20);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();
        textFieldTable.setScale(scaleX,scaleY);
        textFieldTable.setX(DEFAULT_TEXT_FIELD_PADDING_LEFT * scaleX);
        textFieldTable.setY(DEFAULT_TEXT_FIELD_PADDING_BOTTOM * scaleY);
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


}
