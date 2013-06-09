package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Getter
@Setter
@Slf4j
abstract class ClickableActor extends GeActor {

    private static final float SELECTION_ALPHA = 0.5f;
    private static final float SELECTION_SCALE = 0.8f;
    private static final float SELECTION_ALPHA_ACTION_DURATION = 0.5f;
    private static final float SELECTION_SCALE_ACTION_DURATION = 1.5f;

    private ICommand<GePosition> hitCommand;
    private AlphaAction alphaAction;
    private boolean selected;
    private ScaleToAction scaleToAction;

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
                        addAction(alphaAction);
                        addAction(scaleToAction);
                    }
                    selected = !selected;

                }
                hitCommand.perform(new GePosition((int)x, (int)y));
            }
        });

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);

        initSelectionActions();
    }

    private void initSelectionActions() {
        alphaAction = new AlphaAction() {
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
        alphaAction.setAlpha(SELECTION_ALPHA);
        alphaAction.setDuration(SELECTION_ALPHA_ACTION_DURATION);

        scaleToAction = new ScaleToAction() {
            @Override
            protected void begin() {
                float scaleX = getStageInfo().getScaleX();
                float scaleY = getStageInfo().getScaleY();
                float initialEndScaleX = actor.getScaleX() * SELECTION_SCALE;
                float initialScaleY = actor.getScaleY() * SELECTION_SCALE;

                startX = startX == initialEndScaleX ? scaleX : initialEndScaleX;
                startY = startY == initialScaleY ? scaleY : initialScaleY;

                setScale(getX() == scaleX ? scaleX * SELECTION_SCALE : scaleX,
                        getY() == scaleY ? scaleY * SELECTION_SCALE : scaleY);
            }

            @Override
            protected void end() {
                if (selected) {
                    setReverse(!isReverse());
                    restart();
                } else {
                    setScale(getStageInfo().getScaleX(), getStageInfo().getScaleY());
                }
            }
        };
        scaleToAction.setDuration(SELECTION_SCALE_ACTION_DURATION);
    }
}
