package arch.galaxyeclipse.client.ui.widget;

import arch.galaxyeclipse.client.data.GeShipStateInfoHolder;
import arch.galaxyeclipse.client.data.GeShipStaticInfoHolder;
import arch.galaxyeclipse.client.resource.IGeResourceLoader;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GeStateWidget extends Table {

    private static final int DEFAULT_WIDTH = 398;
    private static final int DEFAULT_HEIGHT = 345;
    private static final int HP_LABEL_PADDING_BOTTOM = 214;
    private static final int HP_LABEL_CENTER_PADDING_LEFT = 230;
    private static final int HP_LINE_PADDING_BOTTOM = 180;
    private static final int HP_LINE_PADDING_LEFT = 30;
    private static final int HP_INNER_PADDING_BOTTOM = 190;
    private static final int HP_INNER_PADDING_LEFT = 105;
    private static final int ENERGY_LABEL_PADDING_BOTTOM = 134;
    private static final int ENERGY_LABEL_CENTER_PADDING_LEFT = 175;
    private static final int ENERGY_LINE_PADDING_BOTTOM = 100;
    private static final int ENERGY_LINE_PADDING_LEFT = 30;
    private static final int ENERGY_INNER_PADDING_BOTTOM = 110;
    private static final int ENERGY_INNER_PADDING_LEFT = 50;

    private IGeResourceLoader resourceLoader;

    private ScrollPane hpScrollPane;
    private Image hpLineImage;
    private Image hpInnerImage;
    private Label hpLabel;
    private Table hpLabelTable;
    private ScrollPane energyScrollPane;
    private Image energyLineImage;
    private Image energyInnerImage;
    private Label energyLabel;
    private Table energyLabelTable;

    public GeStateWidget() {
        resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);
        setBackground(resourceLoader.createDrawable("ui/state/state"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        hpInnerImage = new Image(resourceLoader.createDrawable("ui/state/hpInner"));
        ScrollPane.ScrollPaneStyle hpscrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        hpScrollPane = new ScrollPane(hpInnerImage, hpscrollPaneStyle);
        hpScrollPane.setFillParent(false);
        addActor(hpScrollPane);

        hpLineImage = new Image(resourceLoader.createDrawable("ui/state/hpLine"));
        addActor(hpLineImage);

        Label.LabelStyle hpLabelStyle = new Label.LabelStyle(
                resourceLoader.getFont("font_calibri_36px"), Color.WHITE);
        hpLabel = new Label("", hpLabelStyle);
        hpLabelTable = new Table();
        hpLabelTable.setTransform(true);
        hpLabelTable.row();
        hpLabelTable.add(hpLabel);
        addActor(hpLabelTable);

        energyInnerImage = new Image(resourceLoader.createDrawable("ui/state/energyInner"));
        ScrollPane.ScrollPaneStyle energyscrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        energyScrollPane = new ScrollPane(energyInnerImage, energyscrollPaneStyle);
        energyScrollPane.setFillParent(false);
        addActor(energyScrollPane);

        energyLineImage = new Image(resourceLoader.createDrawable("ui/state/energyLine"));
        addActor(energyLineImage);

        Label.LabelStyle energyLabelStyle = new Label.LabelStyle(
                resourceLoader.getFont("font_calibri_36px"), Color.WHITE);
        energyLabel = new Label("", energyLabelStyle);
        energyLabelTable = new Table();
        energyLabelTable.setTransform(true);
        energyLabelTable.row();
        energyLabelTable.add(energyLabel);
        addActor(energyLabelTable);
    }

    @Override
    public void setSize(float width, float height) {
        float scaleX = width / getPrefWidth();
        float scaleY = height / getPrefHeight();

        hpScrollPane.setScale(scaleX, scaleY);
        hpScrollPane.setWidth(hpInnerImage.getWidth());
        hpScrollPane.setHeight(hpInnerImage.getHeight());
        hpScrollPane.setX((float) HP_INNER_PADDING_LEFT * scaleX);
        hpScrollPane.setY((float) HP_INNER_PADDING_BOTTOM * scaleY);

        hpLineImage.setScale(scaleX, scaleY);
        hpLineImage.setX((float) HP_LINE_PADDING_LEFT * scaleX);
        hpLineImage.setY((float) HP_LINE_PADDING_BOTTOM * scaleY);

        hpLabelTable.setScale(scaleX, scaleY);
        hpLabelTable.setX((((float) HP_LABEL_CENTER_PADDING_LEFT) * scaleX) - (hpLabelTable.getWidth() * scaleX / 2f));
        hpLabelTable.setY(((float) HP_LABEL_PADDING_BOTTOM) * scaleY);

        energyScrollPane.setScale(scaleX, scaleY);
        energyScrollPane.setWidth(energyInnerImage.getWidth());
        energyScrollPane.setHeight(energyInnerImage.getHeight());
        energyScrollPane.setX((float) ENERGY_INNER_PADDING_LEFT * scaleX);
        energyScrollPane.setY((float) ENERGY_INNER_PADDING_BOTTOM * scaleY);

        energyLineImage.setScale(scaleX, scaleY);
        energyLineImage.setX((float) ENERGY_LINE_PADDING_LEFT * scaleX);
        energyLineImage.setY((float) ENERGY_LINE_PADDING_BOTTOM * scaleY);

        energyLabelTable.setScale(scaleX, scaleY);
        energyLabelTable.setX((((float) ENERGY_LABEL_CENTER_PADDING_LEFT) * scaleX) - (energyLabelTable.getWidth() * scaleX / 2f));
        energyLabelTable.setY(((float) ENERGY_LABEL_PADDING_BOTTOM) * scaleY);

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
        GeShipStateInfoHolder shipStateInfoHolder = GeContextHolder.getBean(GeShipStateInfoHolder.class);
        GeShipStaticInfoHolder shipStaticInfoHolder = GeContextHolder.getBean(GeShipStaticInfoHolder.class);

        int hp = shipStateInfoHolder.getHp();
        int hpMax = shipStaticInfoHolder.getHpMax();

        hpLabel.setText("" + hp + "/" + hpMax);

        float hpScrollPaneWidth = (hpInnerImage.getWidth()) / ((float) hpMax) * ((float) hp);
        hpScrollPane.setWidth(hpScrollPaneWidth);
        hpScrollPane.invalidate();

        float hpLabelX = (((float) HP_LABEL_CENTER_PADDING_LEFT) * getScaleX()) - (hpLabel.getWidth() * getScaleX() / 2f);
        hpLabel.setX(hpLabelX);
        hpLabel.invalidate();
        hpLabelTable.invalidate();

        int energy = shipStateInfoHolder.getEnergy();
        int energyMax = shipStaticInfoHolder.getEnergyMax();

        energyLabel.setText("" + energy + "/" + energyMax);

        float energyScrollPaneWidth = (energyInnerImage.getWidth()) / ((float) energyMax) * ((float) energy);
        energyScrollPane.invalidate();
        energyScrollPane.setWidth(energyScrollPaneWidth);

        float energyLabelX = (((float) ENERGY_LABEL_CENTER_PADDING_LEFT) * getScaleX()) - (energyLabel.getWidth() * getScaleX() / 2f);
        energyLabel.setX(energyLabelX);
        energyLabel.invalidate();
        energyLabelTable.invalidate();

        super.draw(batch, parentAlpha);
    }

}
