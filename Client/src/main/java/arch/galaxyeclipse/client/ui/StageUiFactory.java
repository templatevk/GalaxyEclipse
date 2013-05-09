package arch.galaxyeclipse.client.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 */
@Slf4j
public class StageUiFactory {
    private StageUiFactory() {
    }

    public static IButtonBuilder createButtonBuilder() {
        return new DefaultButtonBuilder();
    }

    public static  ITextFieldBuilder createTextFieldBuilder() {
        return new DefaultTextFieldBuilder();
    }

    public static void applyTabOrder(List<Actor> actors, Stage stage) {
        for (int i = 0; i < actors.size() - 1; i++) {
            Actor from = actors.get(i);
            Actor to = actors.get(i + 1);
            addTabListener(from, to, stage);
        }
        addTabListener(actors.get(actors.size() - 1), actors.get(0), stage);
    }

    public static void setDefaultButton(List<Actor> actors, Button defaultButton) {
        for (Actor actor : actors) {
            addEnterListener(actor, defaultButton);
        }
    }

    private static void addTabListener(final Actor from, final Actor to, final Stage stage) {
        from.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.TAB) {
                    stage.setKeyboardFocus(to);
                }
                return super.keyTyped(event, character);
            }
        });
    }

    private static void addEnterListener(final Actor actor, final Button defaultButton){
        actor.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    defaultButton.getClickCommand().execute(null,0,0);
                }
                return super.keyTyped(event, character);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    defaultButton.setPressedStyle(true);
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (event.getKeyCode() == Input.Keys.ENTER) {
                    defaultButton.setPressedStyle(false);
                }
                return super.keyUp(event, keycode);
            }
        });
    }
}
