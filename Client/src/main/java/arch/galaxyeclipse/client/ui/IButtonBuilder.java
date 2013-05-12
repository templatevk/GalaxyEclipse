package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 *
 */
public interface IButtonBuilder extends IBuilder<TextButton> {
    enum ButtonType {
        MAIN_MENU_BUTTON,
        GAME_CHAT_HIDE_BUTTON,
        GAME_CHAT_INNER_SCROLL_UP_BUTTON,
        GAME_CHAT_INNER_SCROLL_DOWN_BUTTON,
        GAME_CHAT_INNER_AUTO_SCROLL_BUTTON
    }

    IButtonBuilder setType(ButtonType buttonType);
    IButtonBuilder setText(String text);
    IButtonBuilder setClickCommand(IButtonClickCommand command);
}
