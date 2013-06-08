package arch.galaxyeclipse.client.ui.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 */
public interface IGeActor extends Comparable<IGeActor> {

    void adjust(StageInfo stageInfo);

    ActorType getActorType();

    Actor toActor();
}
