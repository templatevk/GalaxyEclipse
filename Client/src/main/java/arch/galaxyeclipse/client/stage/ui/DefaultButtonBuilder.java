package arch.galaxyeclipse.client.stage.ui;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class DefaultButtonBuilder implements IButtonBuilder {
    public static final int TEXT_PADDING_Y = -10;
    private static final float BUTTON_DOWN_OFFSET = 2;

    private String text;
    private ClickListener listener;
    private IButtonClickCommand connectButtonCommand;
    private TextButton.TextButtonStyle style;

    public DefaultButtonBuilder() {
        text = "";
        listener = new ClickListener();

        IResourceLoader resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        BitmapFont font = resourceLoader.getFont("assets/font_calibri_48px");

        style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnUp"));
        style.down = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnDown"));
        style.font = font;
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.GRAY;
        style.pressedOffsetX = BUTTON_DOWN_OFFSET;
        style.pressedOffsetY = -BUTTON_DOWN_OFFSET;
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
        button.setTextPaddingY(TEXT_PADDING_Y);
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
        button.addListener(new InputListener(){
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                switch(event.getKeyCode()) {
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
