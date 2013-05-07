package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.resource.IResourceLoader;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
class ActorFactory implements IActorFactory {
    private static final String PLAYER_IMAGE_PATH =  // <id>
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
    public LocationObjectActor createLocationObjectActor(LocationObjectPacket locationObject) {
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
    public IGeActor createBackgroundActor(int locationId) {
        String path = String.format(LOCATION_BACKGROUND_IMAGE_PATH, locationId);
        Drawable drawable = new TextureRegionDrawable(resourceLoader.findRegion(path));
        BackgroundActor background = new BackgroundActor(drawable);

        return background;
    }
}
