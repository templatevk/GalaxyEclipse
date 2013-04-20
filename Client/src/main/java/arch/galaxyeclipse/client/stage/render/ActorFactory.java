package arch.galaxyeclipse.client.stage.render;

import arch.galaxyeclipse.client.resource.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.types.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
class ActorFactory implements IActorFactory {
    private static final String FORMAT_SHIP_TYPE_IMAGE_PATH = // <id>
            "ship_types/%1$i/image";
    private static final String FORMAT_STATIC_OBJECT_IMAGE_PATH = // <object_type>, <id>
            "static/%1$s/$2%h";
    private final DictionaryTypesMapper dictionaryTypesMapper;
    private IResourceLoader resourceLoader;


    ActorFactory() {
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
    }

    @Override
    public StageActor createActor(GeProtocol.LocationInfo.LocationObject locationObject) {
        StageActor stageActor = new StageActor();

        String path = null;
        LocationObjectTypesMapperType objectType = dictionaryTypesMapper
                .getLocationObjectTypeById(locationObject.getObjectTypeId());
        switch (objectType) {
            case ROCKET:
                break;
            case ASTEROID:
                break;
            case STATION:
                break;
            case PLAYER:
                break;
        }

        // TODO
        Drawable drawable = new TextureRegionDrawable(resourceLoader.findRegion(path));
        stageActor.setDrawable(drawable);

        return stageActor;
    }
}
