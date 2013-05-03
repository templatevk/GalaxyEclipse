package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 *
 */
public interface IButtonBuilder extends IBuilder<TextButton> {
    IButtonBuilder setText(String text);

    IButtonBuilder setClickCommand(IButtonClickCommand command);
}
