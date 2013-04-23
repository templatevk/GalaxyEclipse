package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.data.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import arch.galaxyeclipse.shared.types.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.*;

/**
 *
 */
@Data
public class GameActor extends Image {
    private LocationObject locationObject;
    private LocationObjectTypesMapperType actorType;
    private ICommand<GePosition> hitCommand;

    public GameActor(Drawable drawable, LocationObject locationObject) {
        super(drawable);
        this.locationObject = locationObject;
        hitCommand = new StubCommand<>();

        setOrigin(getPrefWidth() / 2, getPrefHeight() / 2);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        hitCommand.perform(new GePosition(x, y));
        return super.hit(x, y, touchable);
    }
}
