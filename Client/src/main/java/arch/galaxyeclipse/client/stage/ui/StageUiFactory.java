package arch.galaxyeclipse.client.stage.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.*;
import java.util.List;

/**
 *
 */
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
            addListener(from, to, stage);
        }
        addListener(actors.get(actors.size() - 1), actors.get(0), stage);
    }

    private static void addListener(final Actor from, final Actor to, final Stage stage) {
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
}
