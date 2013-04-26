package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
@Data
class ClickableActor extends GeActor {
    private ICommand<GePosition> hitCommand;

    public ClickableActor() {
        this(null);
    }

    public ClickableActor(Drawable drawable) {
        this(drawable, new StubCommand<GePosition>());
    }

    public ClickableActor(Drawable drawable, ICommand<GePosition> hitCommand) {
        super(drawable);
        this.hitCommand = hitCommand;

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        hitCommand.perform(new GePosition(x, y));
        return super.hit(x, y, touchable);
    }
}
