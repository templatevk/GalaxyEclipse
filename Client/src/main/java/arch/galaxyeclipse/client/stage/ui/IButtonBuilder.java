package arch.galaxyeclipse.client.stage.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
public interface IButtonBuilder extends IBuilder<TextButton> {
    IButtonBuilder setText(String text);

    IButtonBuilder setClickCommand(IButtonClickCommand command);

    IButtonBuilder setClickListener(ClickListener listener);
}
