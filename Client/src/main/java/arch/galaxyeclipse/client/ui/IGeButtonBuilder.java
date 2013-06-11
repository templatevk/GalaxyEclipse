package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 *
 */
public interface IGeButtonBuilder extends IGeBuilder<TextButton> {
    enum ButtonType {
        MAIN_MENU_BUTTON,
        GAME_CHAT_HIDE_BUTTON,
        GAME_CHAT_INNER_SCROLL_UP_BUTTON,
        GAME_CHAT_INNER_SCROLL_DOWN_BUTTON,
        GAME_CHAT_INNER_AUTO_SCROLL_BUTTON,
        GAME_MINIMAP_HIDE_BUTTON,
        GAME_STATE_HIDE_BUTTON,
        GAME_MAINMENU_HIDE_BUTTON;
    }

    IGeButtonBuilder setType(ButtonType buttonType);
    IGeButtonBuilder setText(String text);
    IGeButtonBuilder setClickCommand(IGeButtonClickCommand command);
}
