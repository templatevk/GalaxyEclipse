package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class MainMenuStage extends Stage {
	public MainMenuStage() {
		//Skin skin = new Skin(Gdx.files.internal("skins/test.js"));
		TextureAtlas atlas = CachingTextureAtlas.getInstance();
		BitmapFont font = new BitmapFont(Gdx.files.internal("textures/default.fnt"), true);
		
		Table table = new Table();
		table.setFillParent(true);
		table.setBackground(new TextureRegionDrawable(atlas.findRegion("mainmenu")));
		addActor(table);
		
		
		Pixmap white = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
		white.setColor(new Color(1f, 1f, 1f, 0.3f));
  		white.fillRectangle(0, 0, 32, 32);
  		Texture whiteTexture = new Texture(white);
		white.dispose();

  		TextureRegionDrawable drawableTexture = new TextureRegionDrawable(new TextureRegion(whiteTexture));
		TextField addressTxt = new TextField("Hello World", new TextFieldStyle(
				font, Color.RED, drawableTexture, drawableTexture, 
				new NinePatchDrawable(new NinePatch(atlas.findRegion("btnUp")))));
		Button connectBtn = new Button(
				new NinePatchDrawable(new NinePatch(atlas.findRegion("btnDownSmall"))),
				new NinePatchDrawable(new NinePatch(atlas.findRegion("btnDownSmall"))));
		Image img = new Image(new NinePatch(atlas.findRegion("btnUp")));
		table.row();
		addActor(img);
		
		connectBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				System.out.println("Hello");
			}
		});
		table.add(addressTxt).expand(true, false);
		table.row();
		table.add(connectBtn).expand(true, false);	
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean actorHit = super.touchDown(screenX, screenY, pointer, button);
		System.out.println("Actor hit = " + actorHit);
		return actorHit;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}
