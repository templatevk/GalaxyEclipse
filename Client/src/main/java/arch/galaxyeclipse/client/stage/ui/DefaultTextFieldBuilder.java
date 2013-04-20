package arch.galaxyeclipse.client.stage.ui;

import arch.galaxyeclipse.client.*;
import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class DefaultTextFieldBuilder implements ITextFieldBuilder {
    private static final int TEXT_PADDING_X = 20;
    private static final int TEXT_PADDING_Y = -5;

    private String text;
    private String messageText;
    private float width;
    private float height;
    private boolean passwordMode;
    private char passwordCharacter;
    private TextField.TextFieldStyle textFieldStyle;

    public DefaultTextFieldBuilder() {
        text = "";
        messageText = "";
        passwordMode = false;
        passwordCharacter = '*';

        IResourceLoader resourceLoader = ContextHolder
                .getBean(IResourceLoader.class);

        Drawable carret = new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        Drawable selection = new TextureRegionDrawable(resourceLoader.findRegion("ui/selection"));
        BitmapFont font = resourceLoader.getFont("assets/font_calibri_48px");

        textFieldStyle = new TextField.TextFieldStyle(font, Color.RED, carret, selection,
                new TextureRegionDrawable(resourceLoader.findRegion("ui/textField")));
        textFieldStyle.fontColor = Color.WHITE;
    }

    @Override
    public ITextFieldBuilder setPasswordCharacter(char character) {
        this.passwordCharacter = character;
        return this;
    }

    @Override
    public ITextFieldBuilder setMessageText(String text) {
        this.messageText = text;
        return this;
    }

    @Override
    public ITextFieldBuilder setPasswordMode(boolean passwordMode) {
        this.passwordMode = passwordMode;
        return this;
    }

    @Override
    public ITextFieldBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    @Override
    public ITextFieldBuilder setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public ITextFieldBuilder setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public TextField build() {
        TextField textField = new TextField(text, textFieldStyle);
        textField.setPasswordMode(passwordMode);
        textField.setMessageText(messageText);
        textField.setPasswordCharacter(passwordCharacter);
        textField.setCustomPrefWidth(width);
        textField.setTextPaddingX(TEXT_PADDING_X);
        textField.setTextPaddingY(TEXT_PADDING_Y);
        textField.setFocusTraversal(false);

        if (GalaxyEclipseClient.getEnvType() == GalaxyEclipseClient.getEnvType().DEV
                && "".equals(text)) {
            textField.setText("test");
        }

        return textField;
    }
}
