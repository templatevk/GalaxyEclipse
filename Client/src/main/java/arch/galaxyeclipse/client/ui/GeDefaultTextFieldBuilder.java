package arch.galaxyeclipse.client.ui;


import arch.galaxyeclipse.client.data.IGeResourceLoader;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 *
 */
class GeDefaultTextFieldBuilder implements IGeTextFieldBuilder {
    private static final int TEXT_PADDING_X = 45;
    private static final int TEXT_PADDING_Y = -5;

    private String text;
    private String messageText;
    private float width;
    private float height;
    private boolean passwordMode;
    private char passwordCharacter;
    private TextField.TextFieldStyle textFieldStyle;

    public GeDefaultTextFieldBuilder() {
        text = "";
        messageText = "";
        passwordMode = false;
        passwordCharacter = '*';

        IGeResourceLoader resourceLoader = GeContextHolder
                .getBean(IGeResourceLoader.class);

        Drawable carret = resourceLoader.createDrawable("ui/carret");
        Drawable selection = resourceLoader.createDrawable("ui/selection");
        BitmapFont font = resourceLoader.getFont("font_calibri_48px");

        textFieldStyle = new TextField.TextFieldStyle(font, Color.RED, carret, selection,
                resourceLoader.createDrawable("ui/textField"));
        textFieldStyle.fontColor = Color.WHITE;
    }

    @Override
    public IGeTextFieldBuilder setPasswordCharacter(char character) {
        this.passwordCharacter = character;
        return this;
    }

    @Override
    public IGeTextFieldBuilder setMessageText(String text) {
        this.messageText = text;
        return this;
    }

    @Override
    public IGeTextFieldBuilder setPasswordMode(boolean passwordMode) {
        this.passwordMode = passwordMode;
        return this;
    }

    @Override
    public IGeTextFieldBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    @Override
    public IGeTextFieldBuilder setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public IGeTextFieldBuilder setText(String text) {
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
        return textField;
    }
}
