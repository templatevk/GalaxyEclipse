package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.GeLocationInfoHolder;
import arch.galaxyeclipse.client.data.GeShipStateInfoHolder;
import arch.galaxyeclipse.client.resource.IGeResourceLoader;
import arch.galaxyeclipse.client.window.IGeClientWindow;
import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.thread.GeTaskRunnablePair;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectTypesMapperType;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;


@Slf4j
public class GeMiniMapWidget extends Table {

    private final int DEFAULT_WIDTH = 570;
    private final int DEFAULT_HEIGHT = 591;

    private final int TABLE_ACTORS_WIDTH = 435;
    private final int TABLE_ACTORS_HEIGHT = 435;
    private final int TABLE_ACTORS_LEFT = 110;
    private final int TABLE_ACTORS_BOTTOM = 80;

    private ScrollPane minimapScrollPane;

    private Table actorsTable;

    private IGeResourceLoader resourceLoader;

    private GeDictionaryTypesMapper dictionaryTypesMapper;
    private GeShipStateInfoHolder shipStateInfoHolder;
    private GeLocationInfoHolder locationInfoHolder;
    private RotationTask rotationTask;

    private Image doplerImage;

    public GeMiniMapWidget() {
        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);
        shipStateInfoHolder = GeContextHolder.getBean(GeShipStateInfoHolder.class);
        locationInfoHolder = GeContextHolder.getBean(GeLocationInfoHolder.class);

        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        setBackground(resourceLoader.createDrawable("ui/minimap/minimap"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        actorsTable = new Table();

        ScrollPane.ScrollPaneStyle minimapScrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        minimapScrollPane = new ScrollPane(actorsTable, minimapScrollPaneStyle);
        addActor(minimapScrollPane);

        doplerImage = new Image(resourceLoader.createDrawable("ui/minimap/dopler"));

        rotationTask = new RotationTask();
        rotationTask.start();
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();

        minimapScrollPane.setPosition(TABLE_ACTORS_LEFT * scaleX,TABLE_ACTORS_BOTTOM * scaleY);
        minimapScrollPane.setSize(TABLE_ACTORS_WIDTH * scaleX, TABLE_ACTORS_HEIGHT * scaleY);

        actorsTable.setScale(scaleX, scaleY);
        actorsTable.setSize(TABLE_ACTORS_WIDTH * scaleX, TABLE_ACTORS_HEIGHT * scaleY);

        float doplerImageWidth = (float) TABLE_ACTORS_WIDTH / locationInfoHolder.getWidth() * GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY * 2f;
        float doplerImageHeight = (float) TABLE_ACTORS_HEIGHT / locationInfoHolder.getHeight() * GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY * 2f;
        doplerImage.setSize(doplerImageWidth, doplerImageHeight);
        doplerImage.setScale(scaleX,scaleY);

        super.setSize(width, height);
    }

    @Override
    public float getPrefWidth() {
        return DEFAULT_WIDTH;
    }

    @Override
    public float getPrefHeight() {
        return DEFAULT_HEIGHT;
    }

    public void setMinimapActors(Collection<GeLocationObjectPacket> minimapActors) {
        actorsTable.clear();
        actorsTable.addActor(doplerImage);

        for (GeLocationObjectPacket minimapActor : minimapActors) {
            Image actorImage = new Image();
            GeLocationObjectTypesMapperType objectType = dictionaryTypesMapper
                    .getLocationObjectTypeById(minimapActor.getObjectTypeId());
            boolean self = false;

            switch (objectType) {
                case PLAYER:
                    self = minimapActor.getObjectId() == shipStateInfoHolder.getLocationObjectId();
                    if (self) {
                        Drawable drawable = resourceLoader.createDrawable("ui/minimap/indicatorSelf");
                        actorImage = new Image(drawable);
                    } else {
                        Drawable drawable = resourceLoader.createDrawable("ui/minimap/indicatorEnemy");
                        actorImage = new Image(drawable);
                    }
                    break;
            }
            float actorPositionX = (float) TABLE_ACTORS_WIDTH * actorsTable.getScaleX()
                    / (float)locationInfoHolder.getWidth() * minimapActor.getPositionX();
            float actorPositionY = (float) TABLE_ACTORS_HEIGHT * actorsTable.getScaleY()
                    / (float)locationInfoHolder.getHeight() * minimapActor.getPositionY();

            if (self) {
                float doplerHalfWidth = doplerImage.getWidth() / 2f;
                float doplerHalfHeight = doplerImage.getHeight() / 2f;
                float doplerImagePositionX = actorPositionX - doplerHalfWidth;
                float doplerImagePositionY = actorPositionY - doplerHalfHeight;

                doplerImage.setOrigin(doplerImage.getWidth()/2f,doplerImage.getHeight()/2f);
                doplerImage.setPosition(doplerImagePositionX, doplerImagePositionY);
            }

            float actorHalfWidth = actorImage.getWidth() * actorsTable.getScaleX() / 2f;
            float actorHalfHeight = actorImage.getHeight() * actorsTable.getScaleY() / 2f;
            float actorImagePositionX = actorPositionX - actorHalfWidth;
            float actorImagePositionY = actorPositionY - actorHalfHeight;

            actorImage.setPosition(actorImagePositionX, actorImagePositionY);
            actorImage.setScale(actorsTable.getScaleX(), actorsTable.getScaleY());
            actorsTable.addActor(actorImage);
        }
    }

    private class RotationTask extends GeTaskRunnablePair<Runnable> implements Runnable {

        private static final int DEGREES_DIFF = -6;
        private static final int MAX_DEGREES = 360;

        private RotationTask() {
            super(IGeClientWindow.RENDER_REQUEST_MILLISECONDS_DELAY);
            setRunnable(this);
        }

        @Override
        public void run() {
            float angle = doplerImage.getRotation() + DEGREES_DIFF;
            if (angle > MAX_DEGREES) {
                angle -= MAX_DEGREES;
            }
            doplerImage.setRotation(angle);
        }
    }
}
