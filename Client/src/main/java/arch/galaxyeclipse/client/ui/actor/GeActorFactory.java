package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.client.resource.IGeResourceLoader;
import arch.galaxyeclipse.shared.GeEnvType;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
class GeActorFactory implements IGeActorFactory {

    private static final String PLAYER_IMAGE_PATH =  // <id>
            "ship_type/%d";
    private static final String FOG_IMAGE_PATH = // <id>
            "static/fog/%d";
    private static final String STAR_IMAGE_PATH = // <id>
            "static/star/%d";
    private static final String LOCATION_BACKGROUND_IMAGE_PATH = // <id>
            "location/%d";

    public static final String DEV_PLEASURE = "pleasure/girls";

    private final GeDictionaryTypesMapper dictionaryTypesMapper;
    private IGeResourceLoader resourceLoader;

    GeActorFactory() {
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);
    }

    @Override
    public GeLocationObjectActor createLocationObjectActor(GeLocationObjectPacket locationObjectPacket) {
        GeLocationObjectTypesMapperType objectType = dictionaryTypesMapper
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

        Preconditions.checkNotNull(path, "Image path is null for type="
                + objectType + ", id=" + locationObjectPacket.getNativeId());
        Drawable drawable = resourceLoader.createDrawable(path);

        GeLocationObjectActor locationObjectActor = new GeLocationObjectActor(
                drawable, locationObjectPacket);
        locationObjectActor.setDrawable(drawable);
        locationObjectActor.setLocationObjectType(objectType);

        return locationObjectActor;
    }

    @Override
    public GeBackgroundActor createBackgroundActor(int locationId) {
        if (GeEnvType.CURRENT == GeEnvType.DEV) {
            return new GeBackgroundActor(resourceLoader.createDrawable(DEV_PLEASURE));
        }

        String path = String.format(LOCATION_BACKGROUND_IMAGE_PATH, locationId);
        Drawable drawable = resourceLoader.createDrawable(path);

        GeBackgroundActor background = new GeBackgroundActor(drawable);

        return background;
    }
}
