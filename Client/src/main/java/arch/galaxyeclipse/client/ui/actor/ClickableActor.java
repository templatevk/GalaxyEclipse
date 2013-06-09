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

    private static final float SELECTION_ALPHA = 0.7f;
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
                        addAction(new SelectionAlphaAction());
                        addAction(new SelectionScaleAction());
                    }
                    selected = !selected;
                }
                hitCommand.perform(new GePosition((int)x, (int)y));
            }
        });

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }

    private class SelectionScaleAction extends ScaleToAction {

        private boolean firstScale = true;

        private SelectionScaleAction() {
            setDuration(SELECTION_SCALE_ACTION_DURATION);
        }

        @Override
        protected void begin() {
            float scaleX = getStageInfo().getScaleX();
            float scaleY = getStageInfo().getScaleY();
            float initialEndScaleX = scaleX * SELECTION_SCALE;
            float initialEndScaleY = scaleY * SELECTION_SCALE;

            if (startX == initialEndScaleX) {
                startX = firstScale ? scaleX : 2 * scaleX - initialEndScaleX;;
                endX = initialEndScaleX;
            } else {
                startX = initialEndScaleX;
                endX = 2 * scaleX - initialEndScaleX;
            }
            if (startY == initialEndScaleY) {
                startY = firstScale ? scaleY : 2 * scaleY - initialEndScaleY;
                endY = initialEndScaleY;
            } else {
                startY = initialEndScaleY;
                endY = 2 * scaleY - initialEndScaleY;
            }
            firstScale = false;
        }

        @Override
        protected void end() {
            if (selected) {
                restart();
            } else {
                setScale(getStageInfo().getScaleX(), getStageInfo().getScaleY());
            }
        }
    }

    private class SelectionAlphaAction extends AlphaAction {

        private SelectionAlphaAction() {
            setAlpha(SELECTION_ALPHA);
            setDuration(SELECTION_ALPHA_ACTION_DURATION);
        }

        @Override
        protected void end() {
            if (selected) {
                setAlpha(getAlpha() == SELECTION_ALPHA ? 1f : SELECTION_ALPHA);
                restart();
            } else {
                ClickableActor.this.getColor().a = 1f;
            }
        }
    }
}
