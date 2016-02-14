package scenes;

import managers.Colors;
import managers.GameManager;
import objects.BrushTeeth;
import objects.GameHud;
import objects.GoingToBed;
import objects.Reading;
import objects.Sleepy;
import objects.Snacking;
import objects.TakeAShower;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseCircularOut;

import android.graphics.Color;

import com.visivaemobile.itsbedtime.GameActivity;

public class GameScene extends Scene implements IOnSceneTouchListener {
	
	protected Engine engine;
    protected GameActivity activity;
    protected GameManager gm;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    
    public Sleepy sleepy;
    
    private Sprite duck, milk, book, toothbrush;
    private float[] duckPosition;
    private float[] milkPosition; 
    private float[] bookPosition;
    private float[] toothbrushPosition;
    public boolean selectorsEnabled = false;
    
	public TakeAShower takeAShower;
	public Snacking snacking;
	public BrushTeeth brushTeeth;
	public Reading reading;
	
	public GoingToBed goingToBed;
	
	
	public GameHud gameHud;
	
	public GameScene() {
		gm = GameManager.getInstance();
        engine = gm.engine;
        activity = gm.activity;
        vbom = gm.vbom;
        camera = gm.camera;
        createScene();
	}
	
	public void createScene() {
		
		duckPosition = new float[] {activity.left + 150, activity.top - 150, activity.left - 150, activity.top - 150};
	    milkPosition = new float[] {activity.left + 150, activity.bottom + 150, activity.left - 150, activity.bottom + 150};
	    bookPosition = new float[] {activity.right - 150, activity.top - 150, activity.right + 150, activity.top - 150};
	    toothbrushPosition = new float[] {activity.right -150, activity.bottom + 150, activity.right + 150, activity.bottom + 150};
		
	    gm.loadGameResources();
	    gm.createMusic();
		
	    setOnSceneTouchListener(this);
		
	    createEnvironment();
		createSleepy();
		createActivitiesSelectors();
		createActivitiesEntities();
		
		gameHud = new GameHud();
		attachChild(gameHud);
		registerTouchArea(gameHud.button);
		registerTouchArea(gameHud.backButton);
		camera.setHUD(gameHud);
		
	}
	
	private void createEnvironment() {
		Gradient g = new Gradient(activity.centerX, activity.centerY, activity.CAMERA_WIDTH, activity.CAMERA_HEIGHT, vbom) {
			@Override
			protected void preDraw(final GLState pGLState, final Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		g.setGradient(Color.parseColor(Colors.dusty_violet), Color.parseColor(Colors.hex906090), 0.0f, 1.0f);
		setBackground(new EntityBackground(g));
		
	}
	
	private void createSleepy() {
		sleepy = new Sleepy(activity.centerX, activity.centerY, gm.sleepyCharacter, vbom);
		attachChild(sleepy);		
	}
	
	private void createActivitiesSelectors() {
		duck = new Sprite(duckPosition[0], duckPosition[1], gm.bathSponge, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(selectorsEnabled) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						hideSelectorAndPlayActivity(0);
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(duck);
		registerTouchArea(duck);
		
		milk = new Sprite(milkPosition[0], milkPosition[1], gm.cookie, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(selectorsEnabled) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						hideSelectorAndPlayActivity(1);
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(milk);
		registerTouchArea(milk);
		
		toothbrush = new Sprite(toothbrushPosition[0], toothbrushPosition[1], gm.toothbrush, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(selectorsEnabled) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						hideSelectorAndPlayActivity(2);
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(toothbrush);
		registerTouchArea(toothbrush);
		
		book = new Sprite(bookPosition[0], bookPosition[1], gm.book, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		        if(selectorsEnabled) {
		        	switch(pTouchEvent.getAction()) {			
					case TouchEvent.ACTION_UP:
						//gm.buttonSound.play();
						hideSelectorAndPlayActivity(3);
						break;
				    }
		        }
    			return false;
		    }
		};
		attachChild(book);
		registerTouchArea(book);
		
		createEntityModifiers();
	}
	
	private void createActivitiesEntities() {
		
		takeAShower = new TakeAShower(activity.centerX, activity.centerY);
		attachChild(takeAShower);
		registerTouchArea(takeAShower.bathSponge);
		
		snacking = new Snacking(activity.centerX, activity.centerY);
		attachChild(snacking);
		registerTouchArea(snacking.cookie);
		
		brushTeeth = new BrushTeeth(activity.centerX, activity.centerY);
		attachChild(brushTeeth);
		registerTouchArea(brushTeeth.brushTeeth);
		
		reading = new Reading(activity.centerX, activity.centerY);
		attachChild(reading);
		registerTouchArea(reading.openedBook);
		
		goingToBed = new GoingToBed(activity.centerX, activity.centerY);
		attachChild(goingToBed);
		registerTouchArea(goingToBed.bed);
		
	}
	
	public void hideSelectorAndPlayActivity(final int activityId) {
		//TODO: change the ease function
		selectorsEnabled = false;
		removeEntityModifiers();
		duck.registerEntityModifier(new MoveModifier(1.0f, duckPosition[0], duckPosition[1], duckPosition[2], duckPosition[3], EaseCircularOut.getInstance()));
		milk.registerEntityModifier(new MoveModifier(1.0f, milkPosition[0], milkPosition[1], milkPosition[2], milkPosition[3], EaseCircularOut.getInstance()));
		book.registerEntityModifier(new MoveModifier(1.0f, bookPosition[0], bookPosition[1], bookPosition[2], bookPosition[3], EaseCircularOut.getInstance()));
		toothbrush.registerEntityModifier(new MoveModifier(1.0f, toothbrushPosition[0], toothbrushPosition[1], toothbrushPosition[2], toothbrushPosition[3], EaseCircularOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				if(activityId == 0) {
					gameHud.setProgressBarLevel(takeAShower.progress);
					takeAShower.showActivity();
				}
				else if(activityId == 1) {
					gameHud.setProgressBarLevel(snacking.progress);
					snacking.showActivity();
				}
				else if(activityId == 2) {
					gameHud.setProgressBarLevel(brushTeeth.progress);
					brushTeeth.showActivity();
				}
				else if(activityId == 3) {
					gameHud.setProgressBarLevel(reading.progress);
					reading.showActivity();
				}
				else if(activityId == 10) {
					goingToBed.showActivity();
				}
				gameHud.backButton.registerEntityModifier(new MoveYModifier(1, activity.top + 30, activity.top - 30));
				gameHud.progressBar.registerEntityModifier(new MoveModifier(1, activity.left - 100, activity.centerY + 20, activity.left + 50, activity.centerY + 20, EaseCircularOut.getInstance()));
				gameHud.backTouchable = true;
			}
		});
	}
	
	public void bringSelectorBack() {
		gameHud.backTouchable = false;
		gameHud.backButton.registerEntityModifier(new MoveYModifier(1, activity.top - 30, activity.top + 30));
		gameHud.progressBar.registerEntityModifier(new MoveModifier(1, activity.left + 50, activity.centerY + 20, activity.left - 100, activity.centerY + 20, EaseCircularOut.getInstance()));
		duck.registerEntityModifier(new MoveModifier(1.0f, duckPosition[2], duckPosition[3], duckPosition[0], duckPosition[1], EaseCircularOut.getInstance()));
		milk.registerEntityModifier(new MoveModifier(1.0f, milkPosition[2], milkPosition[3], milkPosition[0], milkPosition[1], EaseCircularOut.getInstance()));
		book.registerEntityModifier(new MoveModifier(1.0f, bookPosition[2], bookPosition[3], bookPosition[0], bookPosition[1], EaseCircularOut.getInstance()));
		toothbrush.registerEntityModifier(new MoveModifier(1.0f, toothbrushPosition[2], toothbrushPosition[3], toothbrushPosition[0], toothbrushPosition[1], EaseCircularOut.getInstance()) {
			@Override protected void onModifierFinished(IEntity pItem) {
				selectorsEnabled = true;
				createEntityModifiers();
			}
		});
	}
	
	private void createEntityModifiers() {
		duck.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f),	new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1))));
		milk.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f),	new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1))));
		book.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f),	new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1))));
		toothbrush.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(.5f, 1, 1.1f, 1, 1.1f),	new ScaleModifier(.5f, 1.1f, 1, 1.1f, 1))));
	}
	
	private void removeEntityModifiers() {
		duck.clearEntityModifiers();
		milk.clearEntityModifiers();
		book.clearEntityModifiers();
		toothbrush.clearEntityModifiers();		
	}
	

	//Define what happens when the player touches the scene
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
