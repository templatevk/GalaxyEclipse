package arch.galaxyeclipse.client.ui;

import arch.galaxyeclipse.client.data.IResourceLoader;
import arch.galaxyeclipse.client.data.ShipStateInfoHolder;
import arch.galaxyeclipse.shared.context.ContextHolder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StateWidget extends Table {
    private final int DEFAULT_WIDTH = 398;
    private final int DEFAULT_HEIGHT = 345;
    private static final int HP_LABEL_PADDING_BOTTOM = 230;
    private static final int HP_LABEL_PADDING_LEFT = 120;

    private IResourceLoader resourceLoader;

    private Drawable background;

    private Label hpLabel;
    private Table hpLabelTable;

    public StateWidget() {
        resourceLoader = ContextHolder.getBean(IResourceLoader.class);
        background = new TextureRegionDrawable(resourceLoader.findRegion("ui/state/state"));
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());

        Label.LabelStyle hpLabelStyle = new Label.LabelStyle(
                resourceLoader.getFont("font_calibri_36px"), Color.RED);
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

        hpLabelTable.setScale(scaleX,scaleY);
        hpLabelTable.setX((float)HP_LABEL_PADDING_LEFT * scaleX);
        hpLabelTable.setY((float)HP_LABEL_PADDING_BOTTOM * scaleY);

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
        validate();
        ShipStateInfoHolder holder = ContextHolder.getBean(ShipStateInfoHolder.class);
        hpLabel.setText("[TEST] HP = " + holder.getHp());
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }
}
