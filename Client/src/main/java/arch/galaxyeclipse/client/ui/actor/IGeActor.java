package arch.galaxyeclipse.client.ui.actor;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
public interface IGeActor extends Layout {
    void adjust(StageInfo stageInfo);

    ActorType getActorType();

    Actor toActor();
}
