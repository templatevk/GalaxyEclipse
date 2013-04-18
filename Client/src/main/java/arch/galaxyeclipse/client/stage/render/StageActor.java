package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.*;

/**
 *
 */
@Data
public class StageActor extends Image {
    private LocationObject locationObject;

    public StageActor() {
        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }
}
