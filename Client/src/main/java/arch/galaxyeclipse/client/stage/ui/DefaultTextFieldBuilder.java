package arch.galaxyeclipse.client.stage.ui;

import arch.galaxyeclipse.client.resources.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class DefaultTextFieldBuilder implements ITextFieldBuilder {
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

        IResourceLoader resourceLoader = ContextHolder.INSTANCE
                .getBean(IResourceLoaderFactory.class).createResourceLoader();

        Drawable carret = new TextureRegionDrawable(resourceLoader.findRegion("ui/carret"));
        textFieldStyle = new TextField.TextFieldStyle(
                resourceLoader.getFont("assets/font1.fnt"), Color.RED, carret, carret,
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
        CustomTextField textField = new CustomTextField(text, textFieldStyle,
                width, height);
        textField.setPasswordMode(passwordMode);
        textField.setMessageText(messageText);
        textField.setPasswordCharacter(passwordCharacter);
        return textField;
    }
}
