package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
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

    private static final float SELECTION_ALPHA = 0.5f;
    private static final float SELECTION_ALPHA_ACTION_DURATION = 0.5f;

    private ICommand<GePosition> hitCommand;
    private AlphaAction selectAction;
    private boolean selected;

    protected abstract boolean isSelectable();

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
                    if (!selected) {
                        addAction(selectAction);
                    }
                    selected = !selected;

                }
                hitCommand.perform(new GePosition((int)x, (int)y));
            }
        });

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);

        selectAction = new AlphaAction() {
            @Override
            protected void end() {
                if (selected) {
                    setAlpha(getAlpha() == SELECTION_ALPHA ? 1f : SELECTION_ALPHA);
                    restart();
                } else {
                    ClickableActor.this.getColor().a = 1f;
                }
            }
        };
        selectAction.setAlpha(SELECTION_ALPHA);
        selectAction.setDuration(SELECTION_ALPHA_ACTION_DURATION);
//        new ScaleByAction()
    }
}
