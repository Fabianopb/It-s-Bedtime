package managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.IGameInterface.OnCreateResourcesCallback;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import resources.GameResources;
import scenes.GameScene;
import android.graphics.Color;

import com.visivaemobile.itsbedtime.GameActivity;
import com.visivaemobile.itsbedtime.R;

public class GameManager {
	
private static final GameManager INSTANCE = new GameManager();
	
	public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    public static GameManager getInstance() {
        return INSTANCE;
    }
    
  //****************************//
    /* SPLASH RESOURCES AND SCENE */
    //****************************//
    
    private Scene splashScene;
    
    public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	private Sprite splashSprite;
	
	private ITexture splashFontTexture;
	private Font splashFont;
	
	public void createSplashResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	this.splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 52, TextureOptions.BILINEAR);
    	this.splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash-200x52.png", 0, 0);
    	this.splashTextureAtlas.load();
		
		FontFactory.setAssetBasePath("fonts/");    	
    	splashFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		splashFont = FontFactory.createFromAsset(activity.getFontManager(), splashFontTexture, activity.getAssets(), "Sniglet-ExtraBold.ttf", 60.0f, true, Color.parseColor(Colors.blue));
		splashFont.load();
		
		baseFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		baseFont = FontFactory.createFromAsset(activity.getFontManager(), baseFontTexture, activity.getAssets(), "Sniglet-Regular.ttf", 24.0f, true, Color.parseColor(Colors.white));
		baseFont.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {		
		splashScene = new Scene();
		
		splashScene.setBackground(new Background(0, 0, 0));
		
		splashSprite = new Sprite(activity.right - 130, activity.bottom + 55, splash_region, vbom);
		splashScene.attachChild(splashSprite);
		
		Text splashText = new Text(activity.centerX, activity.centerY + 50, splashFont, activity.getResources().getString(R.string.loading), engine.getVertexBufferObjectManager());
		splashScene.attachChild(splashText);
		
		TextOptions options = new TextOptions(AutoWrap.WORDS, activity.CAMERA_WIDTH - 60, HorizontalAlign.LEFT);
		Text splashCredits = new Text(activity.centerX, activity.centerY - 50, baseFont, activity.getResources().getString(R.string.credits), options, vbom);
		splashScene.attachChild(splashCredits);
		
		Text developer = new Text(activity.left + 30, activity.bottom + 60, baseFont, activity.getResources().getString(R.string.developer), vbom);
		developer.setAnchorCenterX(0);
		splashScene.attachChild(developer);
		
		Text hashtags = new Text(activity.left + 30, activity.bottom + 30, baseFont, activity.getResources().getString(R.string.hashtags), vbom);
		hashtags.setAnchorCenterX(0);
		splashScene.attachChild(hashtags);
		
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	//**************************//
	/*     LOAD RESOURCES      */
	//*************************//
	
	private ITexture baseFontTexture;
	public Font baseFont;
	private ITexture boldFontTexture;
	public Font boldFont;
	
	private TexturePackTextureRegionLibrary texturePackLibrary;
	private TexturePack texturePack;
	
	public ITextureRegion sleepyCharacter, toothbrush, book, bathSponge, bathTub, bathHat, bubble, foam, letter, cookie, crumb, bed, blanket, zed;
	
	public ITextureRegion progressBar, button;
	public ITiledTextureRegion rituals, eyes;
	
	public void loadGameResources() {
		
		boldFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		boldFont = FontFactory.createFromAsset(activity.getFontManager(), boldFontTexture, activity.getAssets(), "Sniglet-ExtraBold.ttf", 36.0f, true, Color.parseColor(Colors.blue));
		boldFont.load();
		
		try {
			texturePack = new TexturePackLoader(activity.getTextureManager(), "gfx/").loadFromAsset(activity.getAssets(), "game_resources.xml");
    		texturePack.loadTexture();
    		texturePackLibrary = texturePack.getTexturePackTextureRegionLibrary();
	    } 
	    catch (final TexturePackParseException e) {
	        Debug.e(e);
	    }
		
		sleepyCharacter = texturePackLibrary.get(GameResources.MONSTER_ID);
		eyes = texturePackLibrary.getTiled(GameResources.EYES_ID, 2, 1);
		toothbrush = texturePackLibrary.get(GameResources.TOOTHBRUSH_ID);
		book = texturePackLibrary.get(GameResources.BOOK_ID);
		
		progressBar = texturePackLibrary.get(GameResources.PROGRESS_BAR_ID);
		button = texturePackLibrary.get(GameResources.BUTTON_ID);
		rituals = texturePackLibrary.getTiled(GameResources.RITUALS_ID, 2, 1);
		
		bathHat = texturePackLibrary.get(GameResources.BATHHAT_ID);
		bathSponge = texturePackLibrary.get(GameResources.SPONGE_ID);
		bathTub = texturePackLibrary.get(GameResources.BATHTUB_ID);
		bubble = texturePackLibrary.get(GameResources.BUBBLE_ID);
		
		foam = texturePackLibrary.get(GameResources.FOAM_ID);
		
		letter = texturePackLibrary.get(GameResources.LETTER_ID);
		
		cookie = texturePackLibrary.get(GameResources.COOKIE_ID);
		crumb = texturePackLibrary.get(GameResources.CRUMB_ID);
		
		bed = texturePackLibrary.get(GameResources.BED_ID);
		blanket = texturePackLibrary.get(GameResources.BLANKET_ID);
		
		zed = texturePackLibrary.get(GameResources.Z_ID);
		
	}
	
	public Music music;	
	public void createMusic() {
		MusicFactory.setAssetBasePath("mfx/");
		try {
			music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "windswept.ogg");
			music.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}		
	}
	
	public GameScene gameScene;
	
	public void createGameScene() {		
		gameScene = new GameScene();
	}

}
