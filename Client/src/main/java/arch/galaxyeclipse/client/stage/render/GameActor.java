package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import arch.galaxyeclipse.shared.types.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.*;

/**
 *
 */
@Data
public class GameActor extends Image {
    private LocationObject locationObject;
    private LocationObjectTypesMapperType actorType;

    public GameActor(LocationObject locationObject) {
        this.locationObject = locationObject;

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }
}
