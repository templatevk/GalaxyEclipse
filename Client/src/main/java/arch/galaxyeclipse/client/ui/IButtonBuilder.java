package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 *
 */
public interface IButtonBuilder extends IBuilder<TextButton> {

    enum ButtonType
    {
        MainMenuButton,
        GameChatHideButton
    }
    IButtonBuilder setType(ButtonType buttonType);

    IButtonBuilder setText(String text);

    IButtonBuilder setClickCommand(IButtonClickCommand command);
}
