package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.shared.EnvType;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.LocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
class ActorFactory implements IActorFactory {
    private static final String PLAYER_IMAGE_PATH =  // <id>
            "ship_type/%d";
    private static final String FOG_IMAGE_PATH = // <id>
            "static/fog/%d";
    private static final String STAR_IMAGE_PATH = // <id>
            "static/star/%d";
    private static final String LOCATION_BACKGROUND_IMAGE_PATH = // <id>
            "location/%d";

    public static final String DEV_PLEASURE = "pleasure/girls";

    private final DictionaryTypesMapper dictionaryTypesMapper;
    private IResourceLoader resourceLoader;

    ActorFactory() {
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    public LocationObjectActor createLocationObjectActor(LocationObjectPacket locationObjectPacket) {
        LocationObjectTypesMapperType objectType = dictionaryTypesMapper
                .getLocationObjectTypeById(locationObjectPacket.getObjectTypeId());
        String path = null;

        switch (objectType) {
            case FOG:
                path = String.format(FOG_IMAGE_PATH, locationObjectPacket.getNativeId());
                break;
            case STAR:
                path = String.format(STAR_IMAGE_PATH, locationObjectPacket.getNativeId());
                break;
            case ROCKET:
                break;
            case ASTEROID:
                break;
            case STATION:
                break;
            case PLAYER:
                path = String.format(PLAYER_IMAGE_PATH, locationObjectPacket.getNativeId());
                break;
        }

        Drawable drawable = resourceLoader.createDrawable(path);

        LocationObjectActor locationObjectActor = new LocationObjectActor(
                drawable, locationObjectPacket);
        locationObjectActor.setDrawable(drawable);
        locationObjectActor.setLocationObjectType(objectType);

        return locationObjectActor;
    }

    @Override
    public BackgroundActor createBackgroundActor(int locationId) {
        if (EnvType.CURRENT == EnvType.DEV) {
            return new BackgroundActor(resourceLoader.createDrawable(DEV_PLEASURE));
        }

        String path = String.format(LOCATION_BACKGROUND_IMAGE_PATH, locationId);
        Drawable drawable = resourceLoader.createDrawable(path);

        BackgroundActor background = new BackgroundActor(drawable);

        return background;
    }
}
