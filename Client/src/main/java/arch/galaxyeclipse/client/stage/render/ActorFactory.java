package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.types.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class ActorFactory implements IActorFactory {
    private static final String PLAYER_IMAGE_PATH = // <id>
            "ship_types/%1$h/image";
    private static final String STATIC_OBJECT_IMAGE_PATH = // <object_type>, <id>
            "static/%1$s/$2%h";
    private final DictionaryTypesMapper dictionaryTypesMapper;
    private IResourceLoader resourceLoader;


    ActorFactory() {
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    public GameActor createActor(GeProtocol.LocationInfo.LocationObject locationObject) {
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

        GameActor gameActor = new GameActor(drawable, locationObject);
        gameActor.setDrawable(drawable);
        gameActor.setActorType(objectType);

        return gameActor;
    }
}
