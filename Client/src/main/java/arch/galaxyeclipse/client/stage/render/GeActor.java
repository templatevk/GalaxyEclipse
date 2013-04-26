package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
public class GeActor extends Image {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ActorType actorType;

    public GeActor(Drawable drawable) {
        super(drawable);
    }
}
