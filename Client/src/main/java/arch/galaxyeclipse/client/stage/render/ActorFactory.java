package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.types.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
class ActorFactory implements IActorFactory {
    private static final String PLAYER_IMAGE_PATH = // <id>
            "ship_types/%h";
    private static final String STATIC_OBJECT_IMAGE_PATH = // <object_type>, <id>
            "static/%s/%h";
    private static final String LOCATION_BACKGROUND_IMAGE_PATH = // <id>
            "locations/%h";
    private final DictionaryTypesMapper dictionaryTypesMapper;
    private IResourceLoader resourceLoader;

    private Map<Integer, BackgroundActor> backgrounds;

    ActorFactory() {
        backgrounds = new HashMap<>();
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    public LocationObjectActor createLocationObjectActor(GeProtocol.LocationInfo.LocationObject locationObject) {
        LocationObjectTypesMapperType objectType = dictionaryTypesMapper
                .getLocationObjectTypeById(locationObject.getObjectTypeId());
        String path = null;

        switch (objectType) {
            case ROCKET:
                break;
            case ASTEROID:
                break;
            case STATION:
                break;
            case PLAYER:
                path = String.format(PLAYER_IMAGE_PATH, locationObject.getNativeId());
                break;
        }

        Drawable drawable = new TextureRegionDrawable(resourceLoader.findRegion(path));

        LocationObjectActor locationObjectActor = new LocationObjectActor(
                drawable, locationObject);
        locationObjectActor.setDrawable(drawable);
        locationObjectActor.setLocationObjectType(objectType);

        return locationObjectActor;
    }

    @Override
    public BackgroundActor createBackgroundActor(int locationId) {
        BackgroundActor background = backgrounds.get(locationId);

        if (background == null) {
            String path = String.format(LOCATION_BACKGROUND_IMAGE_PATH, locationId);
            Drawable drawable = new TextureRegionDrawable(resourceLoader.findRegion(path));
            background = new BackgroundActor(drawable);

            backgrounds.put(locationId, background);
        }
        return background;
    }
}
