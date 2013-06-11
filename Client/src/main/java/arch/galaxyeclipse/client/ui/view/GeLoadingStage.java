package arch.galaxyeclipse.client.ui.view;

import arch.galaxyeclipse.client.data.IGeResourceLoader;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 *
 */
public class GeLoadingStage extends GeAbstractGameStage {
    private Table rootTable;
    private Table innerTable;

    public GeLoadingStage() {
        IGeResourceLoader resourceLoader = GeContextHolder.getBean(IGeResourceLoader.class);

        innerTable = new Table();
        innerTable.setBounds(0, 0, 400, 200);
        innerTable.setTransform(true);
        innerTable.setBackground(resourceLoader.createDrawable("ui/progress"));

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setTransform(false);
        rootTable.setBackground(resourceLoader.createDrawable("ui/menu_login"));
        rootTable.add(innerTable);

        addActor(rootTable);
    }

    @Override
    protected Group getScaleGroup() {
        return innerTable;
    }
}