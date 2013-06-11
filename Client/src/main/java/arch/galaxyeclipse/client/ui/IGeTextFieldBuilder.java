package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 *
 */
public interface IGeTextFieldBuilder extends IGeBuilder<TextField> {
    IGeTextFieldBuilder setMessageText(String text);

    IGeTextFieldBuilder setPasswordMode(boolean passwordMode);

    IGeTextFieldBuilder setPasswordCharacter(char character);

    IGeTextFieldBuilder setWidth(float width);

    IGeTextFieldBuilder setHeight(float height);

    IGeTextFieldBuilder setText(String text);
}
