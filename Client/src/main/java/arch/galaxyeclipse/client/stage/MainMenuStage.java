package arch.galaxyeclipse.client.stage;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.client.texture.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lombok.extern.slf4j.*;

import java.net.*;
import java.util.*;
import java.util.List;

@Slf4j
class MainMenuStage extends AbstractGameStage implements IServerPacketListener {
	private static final float TABLE_SPACING = 10;
	private static final float DEFAULT_TEXTFIELD_WIDTH = 370;
	private static final float DEFAULT_TEXTFIELD_HEIGHT = 100;
	private static final float DEFAULT_BUTTON_DOWN_OFFSET = 2;
	
	private IClientNetworkManager networkManager;
	
	private Button connectBtn;
	private TextField usernameTxt;
	private TextField passwordTxt;
	private Table rootTable;
	private Table innerTable;
	
	public MainMenuStage() {
		ITextureAtlas atlas = ContextHolder.INSTANCE
				.getBean(ITextureAtlasFactory.class).createAtlas();
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("assets/font1.fnt"), 
				Gdx.files.internal("assets/font1.png"), false);
		
		Drawable carret = new TextureRegionDrawable(atlas.findRegion("ui/carret"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(font, Color.RED, carret, 
				carret, new TextureRegionDrawable(atlas.findRegion("ui/textField")));
		textFieldStyle.fontColor = Color.WHITE;
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(atlas.findRegion("ui/btnUp"));
		style.down = new TextureRegionDrawable(atlas.findRegion("ui/btnDown"));
		style.font = font;
		style.fontColor = Color.BLACK;
		style.downFontColor = Color.DARK_GRAY;
		style.pressedOffsetX = DEFAULT_BUTTON_DOWN_OFFSET;
		style.pressedOffsetY = -DEFAULT_BUTTON_DOWN_OFFSET;
		
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
		
		usernameTxt = new CustomTextField("", textFieldStyle, DEFAULT_TEXTFIELD_WIDTH,
				DEFAULT_TEXTFIELD_HEIGHT);
		usernameTxt.setMessageText("Enter your username");
		usernameTxt.setLayoutEnabled(true);
		
		passwordTxt = new CustomTextField("", textFieldStyle, DEFAULT_TEXTFIELD_WIDTH,
				DEFAULT_TEXTFIELD_HEIGHT);
		passwordTxt.setPasswordMode(true);
		passwordTxt.setPasswordCharacter('*');
		passwordTxt.setMessageText("Enter your password");
		
		// Connection button
        final ICallback<Boolean> connectionCallback = new ICallback<Boolean>() {
            @Override
            public void onOperationComplete(Boolean isConnected) {
                if (log.isInfoEnabled()) {
                    log.info(getClass().getSimpleName() + " connection callback "
                            + " result = " + isConnected);
                }

                if (isConnected) {
                    AuthRequest request = AuthRequest.newBuilder()
                            .setUsername("")
                            .setPassword("").build();
                    Packet packet = Packet.newBuilder()
                            .setType(Type.AUTH_REQUEST)
                            .setAuthRequest(request).build();
                    networkManager.sendPacket(packet);
                }
            }
        };
		connectBtn = new TextButton("Connect", style);
		connectBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				networkManager.connect(new InetSocketAddress(
						SharedInfo.HOST, SharedInfo.PORT), connectionCallback);
			}
		});
		
		innerTable.add(usernameTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(passwordTxt).expand(true, false).space(TABLE_SPACING);
		innerTable.row();
		innerTable.add(connectBtn).expand(true, false).space(TABLE_SPACING);
		innerTable.setOrigin(innerTable.getPrefWidth() / 2, 
				innerTable.getPrefHeight() / 2);
		
		networkManager = ContextHolder.INSTANCE.getBean(IClientNetworkManager.class);
		networkManager.addListener(this);
	}
	
	@Override
	protected Group getScaleGroup() {
		return innerTable;
	}

	@Override
	public void onPacketReceived(Packet packet) {
        if (log.isInfoEnabled()) {
		    log.info(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

		switch (packet.getType()) {
		case AUTH_RESPONSE:
            boolean success = packet.getAuthResponse().getIsSuccess();
			log.info("Authentication result = " + success);

            if (success) {
                /*
                TODO:
                   Change to loading stage, fill dictionary types
                   and obtain the necessary data, change to appropriate
                   stage and unsubscribe from network manager
               */
            }
			break;
		}
	}

	@Override
	public List<Type> getPacketTypes() {
		return Arrays.asList(Type.AUTH_RESPONSE);
	}
}
