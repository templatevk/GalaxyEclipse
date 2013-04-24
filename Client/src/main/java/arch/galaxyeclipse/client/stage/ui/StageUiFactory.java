package arch.galaxyeclipse.client.stage.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import lombok.extern.slf4j.*;

import java.util.*;
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
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
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
                defaultButton.setPressedStyle(true);
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                defaultButton.setPressedStyle(false);
                return super.keyUp(event, keycode);
            }
        });
    }
}
