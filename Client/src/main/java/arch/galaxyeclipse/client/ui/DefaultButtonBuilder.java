package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.resource.IResourceLoader;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 */
class DefaultButtonBuilder implements IButtonBuilder {

    private static final int TEXT_PADDING_Y = -10;
    private static final float BUTTON_DOWN_OFFSET = 2;

    private String text;
    private ClickListener listener;
    private IButtonClickCommand connectButtonCommand;
    private TextButton.TextButtonStyle style;

    public DefaultButtonBuilder() {
        text = "";
        listener = new ClickListener();
        style = new TextButton.TextButtonStyle();
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        style.font = resourceLoader.getFont("assets/font_calibri_36px");
    }

    @Override
    public IButtonBuilder setType(ButtonType buttonType){
        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        switch (buttonType)
        {
            case MainMenuButton:
                this.style.up = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnUp"));
                this.style.down = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnDown"));
                this.style.font = resourceLoader.getFont("assets/font_calibri_48px");
                this.style.fontColor = Color.WHITE;
                this.style.downFontColor = Color.GRAY;
                this.style.pressedOffsetX = BUTTON_DOWN_OFFSET;
                this.style.pressedOffsetY = -BUTTON_DOWN_OFFSET;
                break;
            case GameChatHideButton:
                this.style.up = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnUpChat"));
                this.style.down = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnUpChat"));
                this.style.font = resourceLoader.getFont("assets/font_calibri_36px");
                this.style.fontColor = Color.WHITE;
                this.style.downFontColor = Color.GRAY;
                this.style.pressedOffsetX = BUTTON_DOWN_OFFSET;
                this.style.pressedOffsetY = -BUTTON_DOWN_OFFSET;
                break;
        }
        return this;
    }

    @Override
    public IButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public IButtonBuilder setClickCommand(final IButtonClickCommand command) {
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
