package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.texture.*;
import arch.galaxyeclipse.shared.inject.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 *
 */
class LoadingStage extends AbstractGameStage {
    private Table rootTable;
    private Table innerTable;

    public LoadingStage() {
        ITextureAtlas atlas = SpringContextHolder.CONTEXT
                .getBean(ITextureAtlasFactory.class).createAtlas();

        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/font1.fnt"),
                Gdx.files.internal("assets/font1.png"), false);

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/menu_login")));
        rootTable.debug();
        addActor(rootTable);

        // Inner table is the root layout container
        innerTable = new Table();
        //innerTable.setBackground(new TextureRegionDrawable(atlas.findRegion("ui/group")));
        innerTable.setBounds(0, 0, 400, 200);
        innerTable.setTransform(true);
        innerTable.debug();
        rootTable.setTransform(false);
        rootTable.add(innerTable);
    }

    @Override
    protected Group getScaleGroup() {
        return innerTable;
    }
}
