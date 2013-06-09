package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.client.data.ShipStaticInfoHolder;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StateWidget extends Table  {
    private final int DEFAULT_WIDTH = 398;
    private final int DEFAULT_HEIGHT = 345;
    private static final int HP_LABEL_PADDING_BOTTOM = 214;
    private static final int HP_LABEL_CENTER_PADDING_LEFT = 230;
    private static final int HP_LINE_PADDING_BOTTOM = 180;
    private static final int HP_LINE_PADDING_LEFT = 30;
    private static final int HP_INNER_PADDING_BOTTOM = 190;
    private static final int HP_INNER_PADDING_LEFT = 105;

    private IResourceLoader resourceLoader;

    private ScrollPane testScrollPane;
    private Image hpLineImage;
    private Image hpInnerImage;
    private Label hpLabel;
    private Table hpLabelTable;

    public StateWidget() {
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        setBackground(resourceLoader.createDrawable("ui/state/state"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        hpInnerImage = new Image(resourceLoader.createDrawable("ui/state/hpInner"));
        //addActor(hpInnerImage);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        testScrollPane = new ScrollPane(hpInnerImage,scrollPaneStyle);
        testScrollPane.setFillParent(false);
        addActor(testScrollPane);

        hpLineImage = new Image(resourceLoader.createDrawable("ui/state/hpLine"));
        addActor(hpLineImage);

        Label.LabelStyle hpLabelStyle = new Label.LabelStyle(
                resourceLoader.getFont("font_calibri_36px"), Color.WHITE);
        hpLabel = new Label("",hpLabelStyle);
        hpLabelTable = new Table();
        hpLabelTable.setTransform(true);
        hpLabelTable.row();
        hpLabelTable.add(hpLabel);
        addActor(hpLabelTable);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();

        testScrollPane.setScale(scaleX,scaleY);
        testScrollPane.setWidth(hpInnerImage.getWidth());
        testScrollPane.setHeight(hpInnerImage.getHeight());
        testScrollPane.setX((float) HP_INNER_PADDING_LEFT * scaleX);
        testScrollPane.setY((float) HP_INNER_PADDING_BOTTOM * scaleY);

        hpLineImage.setScale(scaleX, scaleY);
        hpLineImage.setX((float) HP_LINE_PADDING_LEFT * scaleX);
        hpLineImage.setY((float) HP_LINE_PADDING_BOTTOM * scaleY);

        hpLabelTable.setScale(scaleX, scaleY);
        hpLabelTable.setX(((float)HP_LABEL_CENTER_PADDING_LEFT * scaleX) - (hpLabel.getWidth() / 2));
        hpLabelTable.setY((float) HP_LABEL_PADDING_BOTTOM * scaleY);

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

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        ShipStateInfoHolder shipStateInfoHolder = ContextHolder.getBean(ShipStateInfoHolder.class);
        ShipStaticInfoHolder shipStaticInfoHolder = ContextHolder.getBean(ShipStaticInfoHolder.class);

        int hp = shipStateInfoHolder.getHp();
        int hpMax = shipStaticInfoHolder.getHpMax();

        hpLabel.setText("" + hp + "/" + hpMax);
        testScrollPane.setWidth(hpInnerImage.getWidth() / ((float) hpMax) * ((float) hp));
        testScrollPane.invalidate();
        hpLabel.setX((float)HP_LABEL_CENTER_PADDING_LEFT * getScaleX() - (hpLabel.getWidth() / 2));

        super.draw(batch, parentAlpha);
    }

}
