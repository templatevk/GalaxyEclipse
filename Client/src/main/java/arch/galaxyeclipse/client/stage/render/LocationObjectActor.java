package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import arch.galaxyeclipse.shared.types.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
@Data
public class LocationObjectActor extends ClickableActor {
    private LocationObject locationObject;
    private LocationObjectTypesMapperType locationObjectType;

    public LocationObjectActor(Drawable drawable, LocationObject locationObject) {
        this(drawable, locationObject, new StubCommand<GePosition>());
    }

    public LocationObjectActor(Drawable drawable, LocationObject locationObject,
            ICommand<GePosition> hitCommand) {
        super(drawable, hitCommand);
        this.locationObject = locationObject;
        setActorType(ActorType.LOCATION_OBJECT);
    }
}
