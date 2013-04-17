package arch.galaxyeclipse.client.stage.ui;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class DefaultButtonBuilder implements IButtonBuilder {
    private static final float BUTTON_DOWN_OFFSET = 2;

    private String text;
    private ClickListener listener;
    private TextButton.TextButtonStyle style;

    public DefaultButtonBuilder() {
        text = "";
        listener = new ClickListener();

        IResourceLoader resourceLoader = ContextHolder.INSTANCE.getBean(IResourceLoader.class);

        style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnUp"));
        style.down = new TextureRegionDrawable(resourceLoader.findRegion("ui/btnDown"));
        style.font = resourceLoader.getFont("assets/font1.fnt");
        style.fontColor = Color.BLACK;
        style.downFontColor = Color.DARK_GRAY;
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
        return this;
    }

    @Override
    public IButtonBuilder setClickListener(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public TextButton build() {
        TextButton button = new TextButton(text, style);
        button.addListener(listener);
        return button;
    }
}
