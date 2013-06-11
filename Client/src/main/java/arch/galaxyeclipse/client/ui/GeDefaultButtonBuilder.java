package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.data.IGeResourceLoader;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

/**
 *
 */
class GeDefaultButtonBuilder implements IGeButtonBuilder {

    public static final int CHAT_BUTTONTEXT_CENTER_CORRECTION_Y = -15;
    public static final int MINIMAP_BUTTONTEXT_CENTER_CORRECTION_Y = 15;
    public static final int STATE_BUTTONTEXT_CENTER_CORRECTION_Y = -15;
    public static final int MAINMENU_BUTTONTEXT_CENTER_CORRECTION_Y = 15;

    private static final int TEXT_PADDING_Y = -10;
    private static final float BUTTON_DOWN_OFFSET = 2;

    private String text;
    private ClickListener listener;
    private IGeButtonClickCommand connectButtonCommand;
    private TextButton.TextButtonStyle style;

    private int textCenterCorrectionY = 0;

    private IGeResourceLoader resourceLoader;

    public GeDefaultButtonBuilder() {
        text = "";
        listener = new ClickListener();
        style = new TextButton.TextButtonStyle();
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        //Предварительно требуеться загрузить шрифты тут, непонятно почему. Решить.
        resourceLoader.getFont("font_calibri_48px");
        resourceLoader.getFont("font_calibri_36px");
        resourceLoader.getFont("font_impact_36px");
    }

    @Override
    public IGeButtonBuilder setType(ButtonType buttonType) {
        switch (buttonType) {
            case MAIN_MENU_BUTTON:
                style.up = resourceLoader.createDrawable("ui/btnUp");
                style.down = resourceLoader.createDrawable("ui/btnDown");
                style.font = resourceLoader.getFont("font_calibri_48px");
                break;
            case GAME_CHAT_HIDE_BUTTON:
                style.up = resourceLoader.createDrawable("ui/chat/btnChat");
                style.down = resourceLoader.createDrawable("ui/chat/btnChat");
                style.font = resourceLoader.getFont("font_impact_36px");
                textCenterCorrectionY = CHAT_BUTTONTEXT_CENTER_CORRECTION_Y;
                break;
            case GAME_CHAT_INNER_SCROLL_UP_BUTTON:
                style.up = resourceLoader.createDrawable("ui/chat/btnChatScrollUp");
                style.down = resourceLoader.createDrawable("ui/chat/btnChatScrollUp");
                style.font = resourceLoader.getFont("font_calibri_36px");
                break;
            case GAME_CHAT_INNER_SCROLL_DOWN_BUTTON:
                style.up = resourceLoader.createDrawable("ui/chat/btnChatScrollDown");
                style.down = resourceLoader.createDrawable("ui/chat/btnChatScrollDown");
                style.font = resourceLoader.getFont("font_impact_36px");
                break;
            case GAME_CHAT_INNER_AUTO_SCROLL_BUTTON:
                style.up = resourceLoader.createDrawable("ui/chat/btnChatAutoScroll");
                style.down = resourceLoader.createDrawable("ui/chat/btnChatAutoScroll");
                style.font = resourceLoader.getFont("font_impact_36px");
                break;
            case GAME_MINIMAP_HIDE_BUTTON:
                style.up = resourceLoader.createDrawable("ui/minimap/btnMiniMap");
                style.down = resourceLoader.createDrawable("ui/minimap/btnMiniMap");
                style.font = resourceLoader.getFont("font_impact_36px");
                textCenterCorrectionY = MINIMAP_BUTTONTEXT_CENTER_CORRECTION_Y;
                break;
            case GAME_STATE_HIDE_BUTTON:
                style.up = resourceLoader.createDrawable("ui/state/btnState");
                style.down = resourceLoader.createDrawable("ui/state/btnState");
                style.font = resourceLoader.getFont("font_impact_36px");
                textCenterCorrectionY = STATE_BUTTONTEXT_CENTER_CORRECTION_Y;
                break;
            case GAME_MAINMENU_HIDE_BUTTON:
                style.up = resourceLoader.createDrawable("ui/btnMainMenu");
                style.down = resourceLoader.createDrawable("ui/btnMainMenu");
                style.font = resourceLoader.getFont("font_impact_36px");
                textCenterCorrectionY = MINIMAP_BUTTONTEXT_CENTER_CORRECTION_Y;
                break;
        }
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.GRAY;
        style.pressedOffsetX = BUTTON_DOWN_OFFSET;
        style.pressedOffsetY = -BUTTON_DOWN_OFFSET;
        return this;
    }

    @Override
    public IGeButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public IGeButtonBuilder setClickCommand(final IGeButtonClickCommand command) {
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                command.execute(e, x, y);
            }
        };
        connectButtonCommand = command;
        return this;
    }

    @Override
    public TextButton build() {
        final TextButton button = new TextButton(text, style);
        button.addListener(listener);
        button.setPrefTextPaddingY(TEXT_PADDING_Y);
        button.setClickCommand(connectButtonCommand);
        button.setTextCenterCorrectionY(textCenterCorrectionY);
        button.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    button.setColor(Color.GRAY);
                } else {
                    button.setColor(Color.WHITE);
                }
                super.keyboardFocusChanged(event, actor, focused);
            }
        });
        button.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                switch (event.getKeyCode()) {
                    case Input.Keys.ENTER:
                        listener.clicked(event, 0, 0);
                        button.setPressedStyle(true);
                        break;
                }
                return super.keyTyped(event, character);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    button.setPressedStyle(true);
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    button.setPressedStyle(false);
                }
                return super.keyUp(event, keycode);
            }
        });
        return button;
    }
}
