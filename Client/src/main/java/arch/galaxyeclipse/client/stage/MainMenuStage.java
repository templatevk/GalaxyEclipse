package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.window.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class MainMenuStage extends Stage {
	public MainMenuStage() {
		Skin skin = new Skin(Gdx.files.internal("skins/test.js"));
		Table table = new Table();
		addActor(table);
		table.setFillParent(true);
		
		table.setBackground(new TextureRegionDrawable(CachingTextureAtlas
				.getInstance().findRegion("mainmenu")));
		
		//new TextButtonStyle
		TextButton connectBtn = new TextButton("Connect", skin.get(TextButtonStyle.class));
		table.addActor(connectBtn);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}
