package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.data.GePosition;
import arch.galaxyeclipse.shared.util.ICommand;
import arch.galaxyeclipse.shared.util.StubCommand;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
