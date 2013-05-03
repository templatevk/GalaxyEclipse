package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 *
 */
public interface ITextFieldBuilder extends IBuilder<TextField> {
    ITextFieldBuilder setMessageText(String text);

    ITextFieldBuilder setPasswordMode(boolean passwordMode);

    ITextFieldBuilder setPasswordCharacter(char character);

    ITextFieldBuilder setWidth(float width);

    ITextFieldBuilder setHeight(float height);

    ITextFieldBuilder setText(String text);
}
