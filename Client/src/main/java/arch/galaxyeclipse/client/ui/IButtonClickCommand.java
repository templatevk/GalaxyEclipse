package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 *
 */
public interface IButtonClickCommand {
    void execute(InputEvent e, float x, float y);
}
