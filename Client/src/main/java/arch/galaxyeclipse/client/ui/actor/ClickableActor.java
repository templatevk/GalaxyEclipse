package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
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
    private boolean selected;

    public ClickableActor() {
        this(null, ActorType.UNDEFINED);
    }

    public ClickableActor(Drawable drawable, ActorType actorType) {
        super(drawable, actorType);

        hitCommand = new StubCommand<>();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSelectable()) {
                    selected = !selected;
                    select();
                }
                hitCommand.perform(new GePosition(x, y));
            }
        });

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    private void select() {
        ColorAction colorAction = new ColorAction();
        colorAction.setEndColor(Color.RED);
        colorAction.setDuration(1f);
        addAction(colorAction);
    }
}
