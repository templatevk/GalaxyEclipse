package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Data
@Slf4j
abstract class ClickableActor extends GeActor {
    private ICommand<GePosition> hitCommand;

    public ClickableActor() {
        this(null);
    }

    public ClickableActor(Drawable drawable) {
        super(drawable);

        hitCommand = new StubCommand<>();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hitCommand.perform(new GePosition(x, y));
            }
        });

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }
}
